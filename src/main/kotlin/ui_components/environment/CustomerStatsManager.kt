package ui_components.environment

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember

@Composable
fun CustomerStatsManager(arms: MutableList<String>, customerList: MutableList<String>) {
    val customerMap = customerList.map { it to arms.associateWith { 0.0 }}
    val customers = remember { mutableStateMapOf(*customerMap.toTypedArray()) }
    // For every customer, create text fields for each arm
    for (customer in customers) {
        Text(customer.key)
        for (arm in arms) {
            TextField(
                value = customer.value[arm].toString(),
                onValueChange = { newValue ->
                    val convertedValue = newValue.toDoubleOrNull() ?: 0.0

                    // Create a new inner map and update the value
                    val updatedInnerMap = customer.value.toMutableMap()
                    updatedInnerMap[arm] = convertedValue

                    // Replace the inner map in the outer state
                    customers[customer.key] = updatedInnerMap
                },
                label = { Text(arm) }
            )
        }
    }
}