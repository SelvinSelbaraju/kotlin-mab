package bandits.strategies

import bandits.BanditArm
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.distribution.BetaDistribution




class ThompsonSamplingStrategy(val testParam: Double, val arms: Array<BanditArm>): AbstractStrategy() {
    val random = JDKRandomGenerator()
    var armDistributions = resetArmDistributions(arms)
    var bestArm = random.nextInt(arms.size)

    override fun resetStrategy() {
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

    override fun pickArm(): Int {
        val samples = armDistributions.values.map {
            BetaDistribution(random, it.alpha, it.beta).sample()
        }
        return samples.withIndex().maxBy { it.value }.index
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