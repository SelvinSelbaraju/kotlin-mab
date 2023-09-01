package bandits.environments

import kotlinx.serialization.Serializable

@Serializable
data class CustomerStats(
    var populationProb: Double,
    var armProbs: Map<String, Double>
)

@Serializable
data class Environment(
    var numTrials: Int,
    var numCustomers: Int,
    var arms: Array<String>,
    var customers: Map<String, CustomerStats>
)
