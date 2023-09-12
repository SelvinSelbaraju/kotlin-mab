package bandits.strategies

import kotlin.math.ln
import kotlin.math.sqrt


class UCBStrategy(arms: Array<String>, isContextual: Boolean, private val c: Double): AbstractStrategy(arms, epsilon = 0.0, isContextual) {
    override fun updateStrategy(reward: Int, armName: String, sampledCustomer: String) {
        val customerDistributions = armDistributions[sampledCustomer]!!
        if (reward > 0) {
            customerDistributions[armName]!!.alpha += 1
        } else {
            customerDistributions[armName]!!.beta += 1
        }
    }

    override fun pickArm(sampledCustomer: String, timeStep: Int): String {
        // Add to relevant maps if not seen yet
        if(armDistributions[sampledCustomer].isNullOrEmpty()) {
            armDistributions[sampledCustomer] = getDefaultArmDistributions(arms)
            bestArms[sampledCustomer] = getDefaultBestArms(arms)
        }
        bestArms[sampledCustomer] = findBestArm(sampledCustomer, timeStep)
        return bestArms[sampledCustomer]!!
    }

    private fun findBestArm(sampledCustomer: String, timeStep: Int): String {
        val customerDistributions = armDistributions[sampledCustomer]!!
        // For each arm, calculate the UCB score
        val maxScore = customerDistributions.maxOf {
            // alpha + beta is the number of times arm has been pulled
            val numPulls = it.value.alpha + it.value.beta
            calculateUCBScore(it.value.alpha, numPulls, timeStep)
        }
        val bestArms = customerDistributions.filter{
            val numPulls = it.value.alpha + it.value.beta
            calculateUCBScore(it.value.alpha, numPulls, timeStep) == maxScore
        }.keys.toList()
        return if (bestArms.size > 1) {
            bestArms[random.nextInt(bestArms.size)]
        } else {
            bestArms[0]
        }
    }

    private fun calculateUCBScore(totalReward: Double, numPulls: Double, timeStep: Int): Double {
        // Score = Mean + c * sqrt( ln(timeStep) / numPulls)
        // First term is exploitative
        // Second term larger when arm is relatively less explored
        return (totalReward / numPulls) + (c * sqrt(ln(timeStep.toDouble()) / numPulls))
    }
}