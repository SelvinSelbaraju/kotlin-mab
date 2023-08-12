package bandits

import java.util.Random

class BanditArm(val name: String, private val mean: Double, private val stdDev: Double, private val seed: Long = 42) {
    private val random = Random(seed)

    fun pullArm(): Double {
        return random.nextGaussian() * stdDev + mean
    }
}


