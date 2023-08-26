package bandits.strategies

import bandits.BanditArm
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.distribution.BetaDistribution




class ThompsonSamplingStrategy(val testParam: Double, val arms: Array<String>): AbstractStrategy() {
    val random = JDKRandomGenerator()
    var armDistributions = resetArmDistributions(arms)
    var bestArm = arms[random.nextInt(arms.size)]

    override fun resetStrategy() {
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

    override fun pickArm(): String {
        return bestArm
    }

    private fun findBestArm(): String {
        val samples = armDistributions.mapValues {
            BetaDistribution(random, it.value.alpha, it.value.beta).sample()
        }
        val maxSample = samples.maxOf { it.value }
        val bestArms = samples.filter { it.value == maxSample }.keys.toList()
        return bestArms[random.nextInt(bestArms.size)]
    }
}