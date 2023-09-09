package bandits.strategies

import java.util.Random

class EpsilonGreedyStrategy(var epsilon: Double, val arms: Array<String>): AbstractStrategy() {
    val random = Random()
    var numExplores = 0
    var armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
    var bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])
    init {
        updateInvalidEpsilon()
    }
    private fun updateInvalidEpsilon() {
        epsilon = when {
            epsilon < 0 -> 0.0
            epsilon > 1 -> 1.0
            else -> epsilon
        }
        println("Epsilon is: $epsilon")
    }

    override fun resetStrategy() {
        numExplores = 0
        armDistributions = mutableMapOf("default" to arms.associateWith { ArmInfo(1.0, 1.0) })
        bestArms = mutableMapOf("default" to arms[random.nextInt(arms.size)])
    }

    override fun updateStrategy(reward: Int, armName: String, sampledCustomer: String) {
        if (reward > 0) {
            armDistributions[sampledCustomer]!![armName]!!.alpha += 1
        } else {
            armDistributions[sampledCustomer]!![armName]!!.beta += 1
        }
    }

    override fun pickArm(sampledCustomer: String): String  {
        // Add to relevant maps if not seen yet
        if(armDistributions[sampledCustomer].isNullOrEmpty()) {
            armDistributions[sampledCustomer] = arms.associateWith { ArmInfo(1.0, 1.0) }.toMutableMap()
            bestArms[sampledCustomer] = arms[random.nextInt(arms.size)]
        }
        if (random.nextDouble() < epsilon) {
            numExplores += 1
            return arms[random.nextInt(arms.size)]
        }
        bestArms[sampledCustomer] = findBestArm(sampledCustomer)
        return bestArms[sampledCustomer]!!
    }

    private fun findBestArm(sampledCustomer: String): String {
        val customerDistributions = armDistributions[sampledCustomer]!!
        val maxMean = customerDistributions.values.maxOf { it.alpha / (it.alpha + it.beta) }
        val bestArms = customerDistributions.filter{
            it.value.alpha / (it.value.alpha + it.value.beta) == maxMean
        }.keys.toList()
        return if (bestArms.size > 1) {
            bestArms[random.nextInt(bestArms.size)]
        } else {
            bestArms[0]
        }
    }
}