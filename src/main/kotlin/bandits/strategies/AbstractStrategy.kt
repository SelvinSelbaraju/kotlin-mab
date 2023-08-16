package bandits.strategies

import bandits.BanditArm

abstract class AbstractStrategy {
    abstract fun updateStrategy(reward: Int, arm: BanditArm)
    abstract fun resetStrategy()
    abstract fun pickArm(): Int
}