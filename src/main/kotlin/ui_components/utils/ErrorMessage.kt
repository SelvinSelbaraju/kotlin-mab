package ui_components.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui_components.environment.validation.ErrorSimulationParams
import ui_components.environment.validation.ErrorsCustomerStats

data class Errors(val customerStats: ErrorsCustomerStats = ErrorsCustomerStats(), val simulationParams: ErrorSimulationParams = ErrorSimulationParams())
@Composable
fun ErrorText(error: String?, validText: String) {
    if (error.isNullOrEmpty()) {
        Text(validText)
    } else {
        Text(color = Color.Red, text = error)
    }
}

@Composable
fun ErrorMessage(errors: Errors) {
    ErrorText(errors.customerStats.populationProbs, "Population Probs are valid")
    ErrorText(errors.customerStats.armProbs, "Arm Probs are valid")
    ErrorText(errors.simulationParams.numTrials, "Num Trials is valid")
    ErrorText(errors.simulationParams.numSteps, "Num Steps is valid")
}