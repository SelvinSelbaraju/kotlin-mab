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
                val newCustomers = environment.customers.toMutableMap()
                // For the first customer, check if the arm exists. If not, set it to 0.0 for all customers
                for (arm in newArms) {
                    val firstKey = environment.customers.keys.first()
                    if (arm !in environment.customers[firstKey]!!.armProbs.keys.toList()) {
                        for (customer in newCustomers) {
                            val armProbsCopy = customer.value.armProbs.toMutableMap()
                            armProbsCopy[arm] = 0.0
                            newCustomers[customer.key]!!.armProbs = armProbsCopy
                        }
                    }
                }
                // Remove all arms not in arms
                for (customer in newCustomers) {
                    newCustomers[customer.key]!!.armProbs = newCustomers[customer.key]!!.armProbs.filterKeys { it in newArms }
                }
                environment = environment.copy(arms = newArms.toTypedArray(), customers = newCustomers)
            }
            CustomerManager(environment.customers.keys.toMutableList(), customersReadOnly) {
                newCustomers ->
                environment = environment.copy(customers = newCustomers.associateWith {
                    environment.customers[it] ?: CustomerStats(0.0, environment.arms.associateWith { 0.0 }) })
            }
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            // If an arm doesn't exist in customers, create it

            CustomerStatsManager(environment.arms.toMutableList(), environment.customers.toMutableMap()) {
                newCustomerStats ->
                environment.customers = newCustomerStats
            }
//            SimulationParamManager(simulationParams) { simulationParams.value = it }
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
