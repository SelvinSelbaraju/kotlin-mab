package bandits

import kotlinx.serialization.Serializable

@Serializable
data class CustomerConfig(
    val customers: Map<String, CustomerStats>
)

@Serializable
data class CustomerStats(
    val populationProb: Double,
    val armProbs: Map<String, Double>
)

@Serializable
data class Arm(
    val name: String,
    val mean: Double,
)

@Serializable
data class Environment(
    val numTrials: Int,
    val numSteps: Int,
    val arms: Array<Arm>,
    val customers: Map<String, CustomerStats>
)
