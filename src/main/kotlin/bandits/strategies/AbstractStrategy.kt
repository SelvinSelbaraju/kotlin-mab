package bandits.strategies

import org.apache.commons.math3.random.JDKRandomGenerator

data class ArmInfo(
    var alpha: Double,
    var beta: Double,
)
abstract class AbstractStrategy(val arms: Array<String>, open var epsilon: Double = 0.0, val isContextual: Boolean = false) {
    private val defaultCustomer = "default"
    val random = JDKRandomGenerator()
    var armDistributions = mutableMapOf(defaultCustomer to arms.associateWith { ArmInfo(1.0, 1.0) })
    open var bestArms = mutableMapOf(defaultCustomer to arms[random.nextInt(arms.size)])

    abstract fun updateStrategy(reward: Int, armName: String, sampledCustomer: String = defaultCustomer)
    open fun resetStrategy() {
        armDistributions = mutableMapOf(defaultCustomer to arms.associateWith { ArmInfo(1.0, 1.0) })
        bestArms = mutableMapOf(defaultCustomer to arms[random.nextInt(arms.size)])
    }
    abstract fun pickArm(sampledCustomer: String = defaultCustomer): String
    protected fun getDefaultArmDistributions(arms: Array<String>): MutableMap<String, ArmInfo> {
        return arms.associateWith { ArmInfo(1.0, 1.0) }.toMutableMap()
    }

    protected fun getDefaultBestArms(arms: Array<String>): String {
        return arms[random.nextInt(arms.size)]
    }
}