package bandits.strategies

import java.util.Random

class EpsilonGreedyStrategy(var epsilon: Double, val arms: Array<String>): AbstractStrategy() {
    val random = Random()
    var numExplores = 0
    var armDistributions = resetArmDistributions(arms)
    var bestArm = arms[random.nextInt(arms.size)]
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
        armDistributions = resetArmDistributions(arms)
        bestArm = arms[random.nextInt(arms.size)]
    }

    override fun updateStrategy(reward: Int, armName: String) {
        if (reward > 0) {
            armDistributions[armName]!!.alpha += 1
        } else {
            armDistributions[armName]!!.beta += 1
        }
        // Update the best arm
        bestArm = findBestArm()
    }

    override fun pickArm(): String  {
        if (random.nextDouble() < epsilon) {
            numExplores += 1
            return arms[random.nextInt(arms.size)]
        }
        return bestArm
    }

    private fun findBestArm(): String {
        val maxMean = armDistributions.values.maxOf { it.alpha / (it.alpha + it.beta) }
        val bestArms = armDistributions.filter{
            it.value.alpha / (it.value.alpha + it.value.beta) == maxMean
        }.keys.toList()
        return if (bestArms.size > 1) {
            bestArms[random.nextInt(bestArms.size)]
        } else {
            bestArms[0]
        }
    }
}