package ui_components.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ui_components.environment.validation.ErrorsCustomerStats

@Composable
fun ErrorText(error: String?, validText: String) {
    if (error.isNullOrEmpty()) {
        Text(validText)
    } else {
        Text(color = Color.Red, text = error)
    }
}

@Composable
fun ErrorMessage(statsErrors: ErrorsCustomerStats) {
    ErrorText(statsErrors.populationProbs, "Population Probs are valid")
    ErrorText(statsErrors.armProbs, "Arm Probs are valid")
}