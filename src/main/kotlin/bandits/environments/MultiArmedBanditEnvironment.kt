package bandits.environments

import bandits.BanditArm
import bandits.distributions.DiscreteDistribution
import bandits.strategies.AbstractStrategy

class MultiArmedBanditEnvironment(val name: String, envConfig: Environment, val strategy: AbstractStrategy) {
    private val customers = envConfig.customers
    private val customerDistribution = DiscreteDistribution(
        envConfig.customers.map { it.value.populationProb },
        envConfig.customers.map { it.key }
    )
    var runningReward = 0.0

    fun step() {
        val sampledCustomer = customerDistribution.sample()
        val armName = strategy.pickArm()
        val trueMean = customers[sampledCustomer]!!.armProbs[armName]!!
        val reward = BanditArm(trueMean).pullArm()
        runningReward += reward
        strategy.updateStrategy(reward, armName)
    }

    fun resetMab() {
        runningReward = 0.0
        strategy.resetStrategy()
    }


}