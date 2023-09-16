package ui_components.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import bandits.environments.CustomerStats
import bandits.environments.Environment
import bandits.environments.MultiArmedBanditEnvironment
import bandits.simulation.MabSimulator
import bandits.simulation.simulateWriteResults
import bandits.strategies.StrategyFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ui_components.environment.validation.*
import ui_components.utils.ErrorMessage
import ui_components.utils.Errors
import ui_components.utils.ToggleButton
import utils.loadJson
import kotlin.time.measureTimedValue

@Composable
fun EnvironmentManager(environment: Environment, onChange: (Environment) -> Unit) {
    val environmentConstraints = loadJson<EnvironmentConstraints>("src/main/assets/environment_constraints.json")
    val armsReadOnly = remember { mutableStateOf(false) }
    val customersReadOnly = remember { mutableStateOf(false) }
    var results by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(Errors()) }
    val scope = rememberCoroutineScope()
    var simulationJob: Job? by remember {
        mutableStateOf(null)
    }
    var simRunning by remember { mutableStateOf(false) }
    Column() {
        Button(onClick = {
            onChange(loadJson<Environment>("src/main/assets/environment.json"))
            errors = errors.copy(customerStats = validateCustomerStats(environment.customers), simulationParams = validateSimulationParams(environment.numTrials, environment.numSteps, environmentConstraints))
        }) {
            Text("Reset to Default Environment")
        }
        ToggleButton(componentText = "Instructions") {
            EnvironmentInstructions()
        }
        Row {
            ListManager(environment.arms.toMutableList(), "Cuisine", armsReadOnly, environmentConstraints.maxArms) {
                newArms ->
                val newCustomers = validateArms(newArms, environment)
                onChange(environment.copy(arms = newArms.toTypedArray(), customers = newCustomers))
            }
            ListManager(environment.customers.keys.toMutableList(), "Customer", customersReadOnly, environmentConstraints.maxCustomers) {
                newCustomers ->
                // If a customer doesn't exist, create customer stats with below defaults
                onChange(environment.copy(customers = newCustomers.associateWith {
                    environment.customers[it] ?: CustomerStats(0.0, environment.arms.associateWith { 0.0 }) }))
            }
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            errors = errors.copy(customerStats = validateCustomerStats(environment.customers), simulationParams = validateSimulationParams(environment.numTrials, environment.numSteps, environmentConstraints))
            CustomerStatsManager(environment.arms.toMutableList(), environment.customers.toMutableMap()) {
                newCustomerStats ->
                onChange(environment.copy(customers = newCustomerStats))
                errors = errors.copy(customerStats = validateCustomerStats(environment.customers))
            }
            SimulationParamManager(SimulationParams(environment.numTrials, environment.numSteps)) {
                newParams ->
                onChange(environment.copy(numTrials = newParams.numTrials, numSteps = newParams.numCustomers))
                errors = errors.copy(simulationParams = validateSimulationParams(environment.numTrials, environment.numSteps, environmentConstraints))
            }
            if (!simRunning) {
                Button(enabled = (errors.customerStats.populationProbs.isNullOrBlank() && errors.customerStats.armProbs.isNullOrBlank() && errors.simulationParams.numTrials.isNullOrBlank() && errors.simulationParams.numSteps.isNullOrBlank()), onClick = {
                    simRunning = true
                    simulationJob = scope.launch {
                        results = "Running simulation..."
                        val (result, duration) = measureTimedValue {
                            val strategy =
                                StrategyFactory().getStrategyFromConfig("src/main/assets/ucb.json", environment.arms)
                            val mab = MultiArmedBanditEnvironment("mab1", environment, strategy)
                            val simulators = arrayOf(
                                MabSimulator(mab, environment.numTrials, environment.numSteps),
                            )
                            simulateWriteResults(simulators)
                        }
                        results = result + ". Took ${duration.inWholeMilliseconds}ms."
                        simRunning = false
                    }
                }) {
                    Text("Start Simulation")
                }
            } else {
                Row {
                    Button(onClick = {
                        simulationJob?.cancel()
                        results = "Simulation cancelled"
                        simRunning = false
                    }) {
                        Text("Cancel Simulation")
                    }
                    CircularProgressIndicator()
                }
            }
            ErrorMessage(errors)
            Text("Results: $results")
            ToggleButton(componentText = "Debugger") {
                UIDebugger(environment)
            }
        }
    }
}
