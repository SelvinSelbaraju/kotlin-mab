package ui_components.environment

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import bandits.environments.CustomerStats

@Composable
fun CustomerStatsManager(arms: MutableList<String>, customerList: MutableList<String>) {
    val customerMap = customerList.map { it to CustomerStats(0.0, arms.associateWith { 0.0 })}
    val customers = remember { mutableStateMapOf(*customerMap.toTypedArray()) }
    // For every customer, create text fields for each arm
    for (customer in customers) {
        Text(customer.key)
        TextField(
            value = customer.value.populationProb.toString(),
            onValueChange = { newValue ->
                val convertedValue = newValue.toDoubleOrNull() ?: 0.0
                val updatedCustomerStats = customer.value.copy()
                updatedCustomerStats.populationProb = convertedValue
                customers[customer.key] = updatedCustomerStats
            },
            label = { Text("Population Prob") }
        )
        for (arm in arms) {
            TextField(
                value = customer.value.armProbs[arm].toString(),
                onValueChange = { newValue ->
                    val convertedValue = newValue.toDoubleOrNull() ?: 0.0

                    // Create a new inner map and update the value
                    val updatedCustomerStats = customer.value.copy()
                    val updatedInnerMap = updatedCustomerStats.armProbs.toMutableMap()
                    updatedInnerMap[arm] = convertedValue
                    updatedCustomerStats.armProbs = updatedInnerMap

                    // Replace the inner map in the outer state
                    customers[customer.key] = updatedCustomerStats
                },
                label = { Text(arm) }
            )
        }
    }
}