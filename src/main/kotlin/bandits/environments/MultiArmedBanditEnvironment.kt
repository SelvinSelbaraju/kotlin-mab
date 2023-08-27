package bandits.environments

import bandits.BanditArm
import bandits.distributions.DiscreteDistribution
import bandits.strategies.AbstractStrategy
import kotlinx.serialization.Serializable

data class EnvironmentHistory(
    val rewards: MutableList<Int>,
    val customers: MutableList<String>,
    val armNames: MutableList<String>,
    val trueMeans: MutableList<Double>,
    val trialNumber: MutableList<Int>,
)

class MultiArmedBanditEnvironment(val name: String, envConfig: Environment, val strategy: AbstractStrategy) {
    private val customers = envConfig.customers
    private val customerDistribution = DiscreteDistribution(
        envConfig.customers.map { it.value.populationProb },
        envConfig.customers.map { it.key }
    )
    private var trialNumber = 1
    var history = EnvironmentHistory(
        mutableListOf<Int>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<Double>(),
        mutableListOf<Int>()
    )


    fun step() {
        val sampledCustomer = customerDistribution.sample()
        val armName = strategy.pickArm()
        val trueMean = customers[sampledCustomer]!!.armProbs[armName]!!
        val reward = BanditArm(trueMean).pullArm()
        storeResults(sampledCustomer, armName, trueMean, reward)
        strategy.updateStrategy(reward, armName)
    }

    fun resetMab() {
        trialNumber += 1
        strategy.resetStrategy()
    }

    private fun storeResults(customer: String, armName: String, mean: Double, reward: Int) {
        history.rewards.add(reward)
        history.customers.add(customer)
        history.armNames.add(armName)
        history.trueMeans.add(mean)
        history.trialNumber.add(trialNumber)
    }


}