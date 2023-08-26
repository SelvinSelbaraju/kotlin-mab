package bandits

import kotlinx.serialization.Serializable

@Serializable
data class Arm(
    val name: String,
    val mean: Double,
)

@Serializable
data class Environment(
    val numTrials: Int,
    val numSteps: Int,
    val arms: Array<Arm>
)
