package bandits.environments

import bandits.simulation.BanditArm
import bandits.distributions.DiscreteDistribution
import bandits.strategies.AbstractStrategy

data class EnvironmentHistory(
    val timeStep: MutableList<Int>,
    val rewards: MutableList<Int>,
    val customers: MutableList<String>,
    val armNames: MutableList<String>,
    val trueMeans: MutableList<Double>,
    val trialNumber: MutableList<Int>,
    val epsilon: MutableList<Double>,
    val bestMeans: MutableList<Double>,
    val regrets: MutableList<Double>
)

class MultiArmedBanditEnvironment(val name: String, envConfig: Environment, val strategy: AbstractStrategy) {
    private val customers = envConfig.customers
    private val customerDistribution = DiscreteDistribution(
        envConfig.customers.map { it.value.populationProb },
        envConfig.customers.map { it.key }
    )
    private val bestArms = findBestArms()
    private var trialNumber = 1
    private var timeStep = 1
    var history = EnvironmentHistory(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )


    fun step() {
        val sampledCustomer = customerDistribution.sample()
        // If not contextual, hide customer type
        val armName = if (strategy.isContextual) {
            strategy.pickArm(sampledCustomer)
        } else {
            strategy.pickArm()
        }
        val trueMean = customers[sampledCustomer]!!.armProbs[armName]!!
        val reward = BanditArm(trueMean).pullArm()
        storeResults(timeStep, sampledCustomer, armName, trueMean, reward)
        if (strategy.isContextual) {
            strategy.updateStrategy(reward, armName, sampledCustomer)
        } else {
            strategy.updateStrategy(reward, armName)
        }
        timeStep += 1
    }

    fun resetMab() {
        trialNumber += 1
        strategy.resetStrategy()
    }

    private fun storeResults(timeStep: Int, customer: String, armName: String, mean: Double, reward: Int) {
        history.timeStep.add(timeStep)
        history.rewards.add(reward)
        history.customers.add(customer)
        history.armNames.add(armName)
        history.trueMeans.add(mean)
        history.trialNumber.add(trialNumber)
        history.epsilon.add(strategy.epsilon)
        // The highest possible mean for that customer
        history.bestMeans.add(bestArms[customer]!!)
        // Regret is the highest mean - mean of arm
        history.regrets.add(bestArms[customer]!! - mean)
    }

    // Find best mean reward for each customer type
    private fun findBestArms(): Map<String, Double> {
        val bestArms = mutableMapOf<String, Double>()
        for (customer in customers) {
            // For each customer, go into the arm probs and get the max
            bestArms[customer.key] = customer.value.armProbs.maxOf { it.value }
        }
        return bestArms.toMap()
    }


}