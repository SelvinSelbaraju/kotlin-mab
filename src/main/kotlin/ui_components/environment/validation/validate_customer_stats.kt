package ui_components.environment.validation

import bandits.environments.CustomerStats

fun validateCustomerStats(customerStats: Map<String, CustomerStats>): Pair<Boolean, String> {
    return checkPopulationProbs(customerStats)
}
fun checkPopulationProbs(customerStats: Map<String, CustomerStats>): Pair<Boolean, String> {
    var probsValid = true
    var errorText = ""
    val invalidCustomers = mutableMapOf<String, Double>()
    var probTotal = 0.0
    // Check if each prob is valid
    // Also check they sum to 1.0
    for (stats in customerStats) {
        val customerProb = stats.value.populationProb
        probTotal += customerProb
        if(customerProb < 0.0 || customerProb > 1.0) {
            invalidCustomers[stats.key] = customerProb
        }
    }
    if(invalidCustomers.isEmpty() && probTotal != 1.0) {
        probsValid = false
        errorText = "Population Probabilities must sum to 1.0!"
    } else if (invalidCustomers.isNotEmpty()) {
        probsValid = false
        errorText = "Invalid Probabilities: $invalidCustomers"
    }
    return Pair(probsValid, errorText)
}