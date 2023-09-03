package ui_components.environment

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import ui_components.utils.convertToDouble
import ui_components.utils.convertToInt

data class SimulationParams(var numTrials: Int, var numCustomers: Int)

@Composable
fun SimulationParamManager(simulationParams: MutableState<SimulationParams>, onParamChange: (SimulationParams) -> Unit) {
    val params = simulationParams.value.copy()
    TextField(
        value = params.numTrials.toString(),
        onValueChange = { newValue: String ->
            params.numTrials = newValue.convertToInt()
            onParamChange(params)
        },
        label = { Text("Num Trials") }
    )
    TextField(
        value = params.numCustomers.toString(),
        onValueChange = { newValue: String ->
            params.numCustomers = newValue.convertToInt()
            onParamChange(params)
        },
        label = { Text("Num Customers") }
    )
}