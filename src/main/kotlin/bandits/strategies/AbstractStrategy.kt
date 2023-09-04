package bandits.strategies

data class ArmInfo(
    var alpha: Double,
    var beta: Double,
)
abstract class AbstractStrategy {
    fun resetArmDistributions(arms: Array<String>): MutableMap<String, ArmInfo> {
        return arms.associateWith { ArmInfo(1.0, 1.0) }.toMutableMap()
    }
    abstract fun updateStrategy(reward: Int, armName: String)
    abstract fun resetStrategy()
    abstract fun pickArm(): String
}