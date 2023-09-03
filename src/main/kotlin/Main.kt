import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import bandits.environments.CustomerStats
import bandits.environments.Environment
import ui_components.environment.*

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    val scrollState = rememberScrollState()
    val arms = remember { mutableStateListOf("New Arm") }
    val customers = remember { mutableStateListOf("New Customer") }
    val armsReadOnly = remember { mutableStateOf(false) }
    val customersReadOnly = remember { mutableStateOf(false) }
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row {
            ArmManger(arms, armsReadOnly)
            CustomerManager(customers, customersReadOnly)
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            val customerMap = customers.map { it to CustomerStats(0.0, arms.associateWith { 0.0 }) }
            val customersStats = remember { mutableStateMapOf(*customerMap.toTypedArray()) }
            val simulationParams = remember { mutableStateOf(SimulationParams(100,10)) }
            CustomerStatsManager(arms, customersStats)
            SimulationParamManager(simulationParams) { simulationParams.value = it }
            val environment = Environment(simulationParams.value.numTrials, simulationParams.value.numCustomers, arms.toTypedArray(), customersStats)
//            Text(environment.numTrials.toString())
//            Text(environment.numCustomers.toString())
//            for (arm in arms) {
//                Text(arm)
//            }
//            for (customer in customersStats) {
//                Text(customer.value.armProbs.toString())
//                Text(customer.value.populationProb.toString())
//            }
        }
    }

}