package ui_components.environment


import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import ui_components.utils.EditButtons

@Composable
fun CustomerManager(customers: MutableList<String>, customersReadOnly: MutableState<Boolean>, onChange: (MutableList<String>) -> Unit) {
    val maxObjects = 5
    Column {
        customers.forEachIndexed { index, arm ->
            TextField(
                value = arm,
                onValueChange = { it: String ->
                    val customersCopy = customers.toMutableList()
                    customersCopy[index] = it
                    onChange(customersCopy)
                },
                label = { Text("Customer $index") },
                readOnly = customersReadOnly.value
            )
        }
        EditButtons(customersReadOnly, customers, "Customer", onChange, maxObjects = maxObjects)
    }
}