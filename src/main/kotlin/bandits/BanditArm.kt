import java.util.Random

class BanditArm(val name: String, private val mean: Double, private val stdDev: Double, private val seed: Long = 42) {
    private val random = Random(seed)
    var runningMean: Double = 0.0
    private var numPulls: Int = 0

    private fun sampleReward(): Double {
        return random.nextGaussian() * stdDev + mean
    }

    private fun updateRunningMean(reward: Double) {
        runningMean = (runningMean * numPulls + reward) / (numPulls + 1)
        numPulls += 1
    }

    fun pullArm(): Double {
        val reward = sampleReward()
        updateRunningMean(reward)
        return reward
    }
}


