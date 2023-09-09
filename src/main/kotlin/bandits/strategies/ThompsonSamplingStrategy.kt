package bandits.strategies

import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.distribution.BetaDistribution




class ThompsonSamplingStrategy(val arms: Array<String>): AbstractStrategy() {
    val random = JDKRandomGenerator()
    var armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
    var bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])

    override fun resetStrategy() {
        armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
        bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])
    }

    override fun updateStrategy(reward: Int, armName: String, sampledCustomer: String) {
        val customerDistributions = armDistributions[sampledCustomer]!!
        if (reward > 0) {
            customerDistributions[armName]!!.alpha += 1
        } else {
            customerDistributions[armName]!!.beta += 1
        }
    }

    override fun pickArm(sampledCustomer: String): String {
        // Add to relevant maps if not seen yet
        if(armDistributions[sampledCustomer].isNullOrEmpty()) {
            armDistributions[sampledCustomer] = arms.associateWith { ArmInfo(1.0, 1.0) }.toMutableMap()
            bestArms[sampledCustomer] = arms[random.nextInt(arms.size)]
        }
        bestArms[sampledCustomer] = findBestArm(sampledCustomer)
        return bestArms[sampledCustomer]!!
    }

    private fun findBestArm(sampledCustomer: String): String {
        val customerDistributions = armDistributions[sampledCustomer]!!
        val samples = customerDistributions.mapValues {
            BetaDistribution(random, it.value.alpha, it.value.beta).sample()
        }
        val maxSample = samples.maxOf { it.value }
        val sampledBestArms = samples.filter { it.value == maxSample }.keys.toList()
        return sampledBestArms[random.nextInt(sampledBestArms.size)]
    }
}