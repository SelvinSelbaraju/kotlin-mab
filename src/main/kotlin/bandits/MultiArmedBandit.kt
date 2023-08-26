package bandits

import bandits.strategies.AbstractStrategy
import bandits.strategies.EpsilonGreedyStrategy

class MultiArmedBandit(public val name: String, val arms: Map<String, BanditArm>, val strategy: AbstractStrategy) {
    var runningReward: Double = 0.0

    fun resetMab() {
        runningReward = 0.0
        strategy.resetStrategy()
    }

    fun step() {
        // Based on strategy, pick an arm and pull it
        val armToPull =strategy.pickArm()
        val reward = arms[armToPull]!!.pullArm()
        runningReward += reward
        strategy.updateStrategy(reward, armToPull)
    }
}