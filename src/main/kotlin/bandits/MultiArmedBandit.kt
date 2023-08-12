package bandits

import bandits.strategies.EpsilonGreedyStrategy

class MultiArmedBandit(public val name: String, val arms: Array<BanditArm>, val strategy: EpsilonGreedyStrategy) {
    var runningReward: Double = 0.0

    fun resetMab() {
        runningReward = 0.0
        for (arm in arms) {
            arm.resetArm()
        }
        strategy.resetStrategy()
    }

    fun step() {
        // Based on strategy, pick an arm and pull it
        val armToPull = arms[strategy.pickArm()]
        val reward = armToPull.pullArm()
        runningReward += reward
        strategy.updateStrategy(reward, armToPull)
    }
}