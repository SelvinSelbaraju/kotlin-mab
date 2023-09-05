package ui_components.environment.validation

import bandits.environments.CustomerStats
import bandits.environments.Environment

//
fun validateArms(newArms: MutableList<String>, environment: Environment): MutableMap<String, CustomerStats> {
    val newCustomers = environment.customers.toMutableMap()
    val updatedNewCustomers = handleMissingArm(newArms, newCustomers, environment)
    return removeDeletedArms(newArms, updatedNewCustomers)
}

fun handleMissingArm(newArms: MutableList<String>, newCustomers: MutableMap<String, CustomerStats>, environment: Environment): MutableMap<String, CustomerStats> {
    for (arm in newArms) {
        val firstKey = environment.customers.keys.first()
        if (arm !in environment.customers[firstKey]!!.armProbs.keys.toList()) {
            for (customer in newCustomers) {
                val armProbsCopy = customer.value.armProbs.toMutableMap()
                armProbsCopy[arm] = 0.0
                newCustomers[customer.key]!!.armProbs = armProbsCopy
            }
        }
    }
    return newCustomers
}

fun removeDeletedArms(newArms: MutableList<String>, newCustomers: MutableMap<String, CustomerStats>): MutableMap<String, CustomerStats> {
    for (customer in newCustomers) {
        newCustomers[customer.key]!!.armProbs = newCustomers[customer.key]!!.armProbs.filterKeys { it in newArms }
    }
    return newCustomers
}