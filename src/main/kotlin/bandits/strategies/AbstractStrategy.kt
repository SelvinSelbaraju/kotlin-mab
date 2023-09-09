package bandits.strategies

import org.apache.commons.math3.random.JDKRandomGenerator
import java.util.*

data class ArmInfo(
    var alpha: Double,
    var beta: Double,
)
abstract class AbstractStrategy(val arms: Array<String>) {
    val random = JDKRandomGenerator()
    var armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
    open var bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])

    abstract fun updateStrategy(reward: Int, armName: String, sampledCustomer: String)
    open fun resetStrategy() {
        armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
        bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])
    }
    abstract fun pickArm(sampledCustomer: String): String
    protected fun getDefaultArmDistributions(arms: Array<String>): MutableMap<String, ArmInfo> {
        return arms.associateWith { ArmInfo(1.0, 1.0) }.toMutableMap()
    }

    protected fun getDefaultBestArms(arms: Array<String>): String {
        return arms[random.nextInt(arms.size)]
    }
}