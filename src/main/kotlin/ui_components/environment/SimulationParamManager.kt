package ui_components.environment

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

data class SimulationParams(var numTrials: Int, var numCustomers: Int)

@Composable
fun SimulationParamManager(simulationParams: MutableState<SimulationParams>, onParamChange: (SimulationParams) -> Unit) {
    val params = simulationParams.value.copy()
    TextField(
        value = params.numTrials.toString(),
        onValueChange = { newValue: String ->
            val convertedValue = newValue.toIntOrNull() ?: 0
            params.numTrials = convertedValue
            onParamChange(params)
        },
        label = { Text("Num Trials") }
    )
    TextField(
        value = params.numCustomers.toString(),
        onValueChange = { newValue: String ->
            val convertedValue = newValue.toIntOrNull() ?: 0
            params.numCustomers = convertedValue
            onParamChange(params)
        },
        label = { Text("Num Trials") }
    )
}