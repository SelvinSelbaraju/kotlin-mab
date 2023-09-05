package ui_components.environment

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun EnvironmentInstructions() {
    Text("""
        Environment Instructions:
        1. Customers and cuisines must be unique
        2. All Probabilities must be >=0, <=1.0
        3. Population Probabilities must sum to 1.0
        4. Max Trials and Steps is 1000
    """.trimIndent())
}