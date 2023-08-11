package bandits

import java.util.Random

class EpsilonGreedyStrategy(private val epsilon: Double) {
    private val random = Random()
    var numExplores = 0

    fun resetStrategy() {
        numExplores = 0
    }

    fun selectArm(arms: Array<BanditArm>, bestArm: BanditArm): BanditArm {
        if (random.nextDouble() < epsilon) {
            numExplores += 1
            return arms[random.nextInt(arms.size)]
        }
        return bestArm
    }
}