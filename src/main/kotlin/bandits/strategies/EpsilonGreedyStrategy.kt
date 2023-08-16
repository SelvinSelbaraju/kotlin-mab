package bandits.strategies

import bandits.BanditArm
import java.util.Random

data class ArmInfo(
    var alpha: Int,
    var beta: Int,
)

class EpsilonGreedyStrategy(var epsilon: Double, val arms: Array<BanditArm>): AbstractStrategy() {
    val random = Random()
    var numExplores = 0
    var armDistributions = resetArmDistributions(arms)
    var bestArm = random.nextInt(arms.size)
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

    private fun resetArmDistributions(arms: Array<BanditArm>): MutableMap<String, ArmInfo> {
        val distributions: MutableMap<String, ArmInfo> = mutableMapOf()
        return arms.associateTo(distributions) { arm ->
            arm.name to ArmInfo(
                1,
                1,
            )
        }
    }

    override fun resetStrategy() {
        numExplores = 0
        armDistributions = resetArmDistributions(arms)
        bestArm = random.nextInt(arms.size)
    }

    override fun updateStrategy(reward: Int, arm: BanditArm) {
        if (reward > 0) {
            armDistributions[arm.name]!!.alpha += 1
        } else {
            armDistributions[arm.name]!!.beta += 1
        }
        // Update the best arm
        bestArm = findBestArm()
    }

    override fun pickArm(): Int  {
        if (random.nextDouble() < epsilon) {
            numExplores += 1
            return random.nextInt(arms.size)
        }
        return bestArm
    }

    private fun findBestArm(): Int {
        val maxMean = armDistributions.values.maxOfOrNull { it.alpha / (it.alpha + it.beta) }
        val bestArms = armDistributions.values.mapIndexedNotNull { index, it ->
            if(it.alpha / (it.alpha + it.beta) == maxMean) index else null
        }
        return if (bestArms.size > 1) {
            bestArms[random.nextInt(bestArms.size)]
        } else {
            bestArms[0]
        }
    }
}