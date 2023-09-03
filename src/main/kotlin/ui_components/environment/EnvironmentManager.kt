package ui_components.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import bandits.environments.CustomerStats
import bandits.environments.Environment
import bandits.environments.MultiArmedBanditEnvironment
import bandits.simulation.MabSimulator
import bandits.simulation.simulateWriteResults
import bandits.strategies.StrategyFactory
import ui_components.utils.ToggleButton
import utils.loadJson

@Composable
fun EnvironmentManager() {
    val scrollState = rememberScrollState()
    val arms = remember { mutableStateListOf("New Arm") }
    val customers = remember { mutableStateListOf("New Customer") }
    val armsReadOnly = remember { mutableStateOf(false) }
    val customersReadOnly = remember { mutableStateOf(false) }
    var results by remember { mutableStateOf("") }
    var environment by remember { mutableStateOf(loadJson<Environment>("src/main/assets/environment.json")) }
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        UIDebugger(environment)
        Row {
            ArmManger(environment.arms.toMutableList(), armsReadOnly) {
                newArms ->
                environment = environment.copy(arms = newArms.toTypedArray())
            }
            CustomerManager(environment.customers.keys.toMutableList(), customersReadOnly) {
                newCustomers ->
                environment = environment.copy(customers = newCustomers.associateWith {
                    environment.customers[it] ?: CustomerStats(0.0, environment.arms.associateWith { 0.0 }) })
            }
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            val customerMap = customers.map { it to CustomerStats(0.0, arms.associateWith { 0.0 }) }
            val customersStats = remember { mutableStateMapOf(*customerMap.toTypedArray()) }
            val simulationParams = remember { mutableStateOf(SimulationParams(100,10)) }
            CustomerStatsManager(arms, customersStats)
            SimulationParamManager(simulationParams) { simulationParams.value = it }
//            val environment = Environment(simulationParams.value.numTrials, simulationParams.value.numCustomers, arms.toTypedArray(), customersStats)
            Button(onClick = {
                val strategy = StrategyFactory().getStrategyFromConfig("src/main/assets/explore_e_greedy.json", arms.toTypedArray())
                val mab = MultiArmedBanditEnvironment("mab1", environment, strategy)
                val simulators = arrayOf(
                    MabSimulator(mab, environment.numTrials, environment.numCustomers)
                )
                results = simulateWriteResults(simulators).toString()
            }) {
                Text("Start Simulation")
            }
            Text("Results: $results")
            ToggleButton(componentText = "Debugger") {
                UIDebugger(environment)
            }
        }
    }
}
