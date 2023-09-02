package ui_components.environment


import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

@Composable
fun CustomerManager(customers: MutableList<String>, customersReadOnly: MutableState<Boolean>) {
    Column {
        customers.forEachIndexed { index, arm ->
            TextField(
                value = arm,
                onValueChange = { it: String ->
                    customers[index] = it
                },
                label = { Text("Customer $index") },
                readOnly = customersReadOnly.value
            )
        }
        Button(onClick = { if (!customersReadOnly.value) {
            customers.add("New Customer")
        } }) {
            Text("Add Customer")
        }
        Button(onClick = { if (!customersReadOnly.value) {
            customers.removeLast()
        } }) {
            Text("Delete")
        }
        Button(onClick = { customersReadOnly.value = !customersReadOnly.value }) {
            if (!customersReadOnly.value) {
                Text("Save Customers")
            } else {
                Text("Edit Customers")
            }

        }
        if (customersReadOnly.value) {
            Text("Customers Saved")
        } else {
            Text("Customers Editable")
        }
    }
}