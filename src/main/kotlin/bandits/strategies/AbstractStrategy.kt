package bandits.strategies

data class ArmInfo(
    var alpha: Double,
    var beta: Double,
)
abstract class AbstractStrategy {
    abstract fun updateStrategy(reward: Int, armName: String, sampledCustomer: String)
    abstract fun resetStrategy()
    abstract fun pickArm(sampledCustomer: String): String
}