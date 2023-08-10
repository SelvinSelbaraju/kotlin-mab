package bandits

import java.util.Random
import BanditArm

abstract class AbstractBanditStrategy {
    abstract fun selectArm(arms: Array<BanditArm>, bestArm: BanditArm): BanditArm
    abstract fun updateStrategy(arm: BanditArm, reward: Double)
}

class EpsilonGreedyStrategy(private val epsilon: Double) {
    private val random = Random()
    var numExplores = 0

    fun selectArm(arms: Array<BanditArm>, bestArm: BanditArm): BanditArm {
        if (random.nextDouble() < epsilon) {
            numExplores += 1
            return arms[random.nextInt(arms.size)]
        }
        return bestArm
    }
}