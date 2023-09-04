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
import ui_components.environment.validation.validateArms
import ui_components.environment.validation.validateCustomerStats
import ui_components.utils.ErrorMessage
import ui_components.utils.ToggleButton
import utils.loadJson

@Composable
fun EnvironmentManager() {
    val scrollState = rememberScrollState()
    val armsReadOnly = remember { mutableStateOf(false) }
    val customersReadOnly = remember { mutableStateOf(false) }
    var results by remember { mutableStateOf("") }
    var environment by remember { mutableStateOf(loadJson<Environment>("src/main/assets/environment.json")) }
    var isError by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row {
            ListManager(environment.arms.toMutableList(), "Cuisine", armsReadOnly, 7) {
                newArms ->
                val newCustomers = validateArms(newArms, environment)
                environment = environment.copy(arms = newArms.toTypedArray(), customers = newCustomers)
            }
            ListManager(environment.customers.keys.toMutableList(), "Customer", customersReadOnly, 5) {
                newCustomers ->
                // If a customer doesn't exist, create customer stats with below defaults
                environment = environment.copy(customers = newCustomers.associateWith {
                    environment.customers[it] ?: CustomerStats(0.0, environment.arms.associateWith { 0.0 }) })
            }
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            CustomerStatsManager(environment.arms.toMutableList(), environment.customers.toMutableMap()) {
                newCustomerStats ->
                environment = environment.copy(customers = newCustomerStats)
                val (statsValid, statsErrorText) = validateCustomerStats(environment.customers)
                isError = !statsValid
                errorText = statsErrorText
            }
            SimulationParamManager(SimulationParams(environment.numTrials, environment.numCustomers)) { newParams -> environment = environment.copy(numTrials = newParams.numTrials, numCustomers = newParams.numCustomers) }
            Button(enabled = !isError, onClick = {
                val strategy = StrategyFactory().getStrategyFromConfig("src/main/assets/explore_e_greedy.json", environment.arms)
                val mab = MultiArmedBanditEnvironment("mab1", environment, strategy)
                val simulators = arrayOf(
                    MabSimulator(mab, environment.numTrials, environment.numCustomers)
                )
                results = simulateWriteResults(simulators).toString()
            }) {
                Text("Start Simulation")
            }
            ErrorMessage(isError, errorText)
            Text("Results: $results")
            ToggleButton(componentText = "Debugger") {
                UIDebugger(environment)
            }
        }
    }
}
