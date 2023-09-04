package ui_components.environment

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ui_components.utils.convertToDouble
import ui_components.utils.convertToInt

data class SimulationParams(var numTrials: Int, var numCustomers: Int)

@Composable
fun SimulationParamManager(simulationParams: SimulationParams, onParamChange: (SimulationParams) -> Unit) {
    val params = simulationParams.copy()
    val scrollState = rememberScrollState()
    Text("Simulation Parameters")
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
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
}