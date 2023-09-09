package bandits.strategies

class EpsilonGreedyStrategy(override var epsilon: Double, private var alpha: Double = 1.0, arms: Array<String>, isContextual: Boolean): AbstractStrategy(arms, epsilon, isContextual) {
    var numExplores = 0
    init {
        epsilon = updateInvalidFrac(epsilon)
        // Alpha is the epsilon decay parameter
        alpha = updateInvalidFrac(alpha)
        println("Epsilon is $epsilon, Alpha is $alpha")
    }
    private fun updateInvalidFrac(num: Double): Double {
        return when {
            num < 0 -> 0.0
            num > 1 -> 1.0
            else -> num
        }
    }

    override fun resetStrategy() {
        super.resetStrategy()
        numExplores = 0
    }

    override fun updateStrategy(reward: Int, armName: String, sampledCustomer: String) {
        if (reward > 0) {
            armDistributions[sampledCustomer]!![armName]!!.alpha += 1
        } else {
            armDistributions[sampledCustomer]!![armName]!!.beta += 1
        }
        epsilon *= alpha
    }

    override fun pickArm(sampledCustomer: String): String  {
        // Add to relevant maps if not seen yet
        if(armDistributions[sampledCustomer].isNullOrEmpty()) {
            armDistributions[sampledCustomer] = getDefaultArmDistributions(arms)
            bestArms[sampledCustomer] = getDefaultBestArms(arms)
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