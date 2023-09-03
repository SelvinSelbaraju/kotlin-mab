package ui_components.environment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bandits.environments.Environment

// Display Environment as text on screen for debugging
@Composable
fun UIDebugger(environment: Environment) {
    Text("Environment Debugger")
    Text("Num Trials: ${environment.numTrials}")
    Text("Num Customers: ${environment.numCustomers}")
    Row {
        environment.arms.forEachIndexed { index, arm ->
            Text("Arm ${index}: $arm", modifier = Modifier.padding(end=5.dp))
        }
    }
    for (customer in environment.customers) {
        Row {
            Text("Customer: ${customer.key}")
            Text(customer.value.armProbs.toString())
            Text(customer.value.populationProb.toString())
        }
    }
}