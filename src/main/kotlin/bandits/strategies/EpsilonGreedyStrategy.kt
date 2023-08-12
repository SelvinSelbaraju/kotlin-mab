package bandits.strategies

import bandits.BanditArm
import java.util.Random



data class ArmInfo(
    val history: MutableList<Double>,
    var mean: Double,
    var numPulls: Int
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
                mutableListOf(),
                0.0,
                0
            )
        }
    }

    override fun resetStrategy() {
        numExplores = 0
        armDistributions = resetArmDistributions(arms)
    }

    override fun updateStrategy(reward: Double, arm: BanditArm) {
        // Update mean
        val armInfo = armDistributions[arm.name]!!
        armDistributions[arm.name]!!.numPulls += 1
        armDistributions[arm.name]!!.mean = ((armInfo.mean * armInfo.numPulls - 1) + reward) / armInfo.numPulls

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
        val bestArm = armDistributions.entries.withIndex().maxBy { (_,entry) ->
            entry.value.mean
        }.index
        println("Best Arm: $bestArm")
        return bestArm
    }


}