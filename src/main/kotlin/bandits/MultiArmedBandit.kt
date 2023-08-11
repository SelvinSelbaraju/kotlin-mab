package bandits

import BanditArm

class MultiArmedBandit(public val name: String, val arms: Array<BanditArm>, val strategy: EpsilonGreedyStrategy) {
    var bestArm: BanditArm = arms[0]
    var runningReward: Double = 0.0

    private fun findBestArm(): BanditArm {
        var runningBestArm = 0
        for (i in arms.indices) {
            if (arms[i].runningMean > arms[runningBestArm].runningMean) {
                runningBestArm = i
            }
        }

        bestArm = arms[runningBestArm]
        return arms[runningBestArm]
    }

    fun step() {
        // Based on strategy, pick an arm and pull it
        val armToPull = strategy.selectArm(arms, findBestArm())
        runningReward += armToPull.pullArm()
    }
}