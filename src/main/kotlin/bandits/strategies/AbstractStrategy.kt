package bandits.strategies

import bandits.BanditArm

data class ArmInfo(
    var alpha: Double,
    var beta: Double,
)
abstract class AbstractStrategy {
    fun resetArmDistributions(arms: Array<BanditArm>): MutableMap<String, ArmInfo> {
        val distributions: MutableMap<String, ArmInfo> = mutableMapOf()
        return arms.associateTo(distributions) { arm ->
            arm.name to ArmInfo(
                1.0,
                1.0,
            )
        }
    }
    abstract fun updateStrategy(reward: Int, arm: BanditArm)
    abstract fun resetStrategy()
    abstract fun pickArm(): Int
}