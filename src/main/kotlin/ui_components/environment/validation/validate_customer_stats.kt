package ui_components.environment.validation

import bandits.environments.CustomerStats

data class ErrorsCustomerStats(var populationProbs: String? = null, var armProbs: String? = null)

fun validateCustomerStats(customerStats: Map<String, CustomerStats>): ErrorsCustomerStats {
    val populationProbsValid = checkPopulationProbs(customerStats)
    val armProbsValid = checkArmProbs(customerStats)
    return ErrorsCustomerStats(
        populationProbsValid,
        armProbsValid
    )
}

fun checkArmProbs(customerStats: Map<String, CustomerStats>): String? {
    val invalidArmProbs = mutableMapOf<String, MutableMap<String,Double> >()
    for (customer in customerStats) {
        for (arm in customer.value.armProbs) {
            if (arm.value < 0.0 || arm.value > 1.0) {
                if (!invalidArmProbs[customer.key].isNullOrEmpty()) {
                    invalidArmProbs[customer.key]!![arm.key] = arm.value
                } else {
                    invalidArmProbs[customer.key] = mutableMapOf(arm.key to arm.value)
                }
            }
        }
    }
    return if (invalidArmProbs.isNotEmpty()) {
        "Invalid Arm Probs: $invalidArmProbs"
    } else {
        null
    }
}

fun checkPopulationProbs(customerStats: Map<String, CustomerStats>): String? {
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
        errorText = "Population Probabilities must sum to 1.0!"
    } else if (invalidCustomers.isNotEmpty()) {
        errorText = "Invalid Probabilities: $invalidCustomers"
    }
    return errorText
}