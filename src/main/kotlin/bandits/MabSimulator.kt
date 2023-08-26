package bandits

import bandits.environments.MultiArmedBanditEnvironment

class MabSimulator(val mab: MultiArmedBanditEnvironment, val numTrials: Int, val numCustomers: Int) {
    var trialRewards: MutableList<Double> = mutableListOf()

    // Perform a trial which is a collection of steps
    private fun simulateTrial() {
        for (i in 1..numCustomers) {
            mab.step()
        }
        trialRewards.add(mab.runningReward)
    }

    fun simulate() {
        for (i in 1..numCustomers) {
            simulateTrial()
            mab.resetMab()
        }
    }
}