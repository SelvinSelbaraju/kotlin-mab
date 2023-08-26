package bandits

import bandits.environments.EnvironmentHistory
import bandits.environments.MultiArmedBanditEnvironment

class MabSimulator(val mab: MultiArmedBanditEnvironment, val numTrials: Int, val numCustomers: Int) {
    var trialHistories: MutableList<EnvironmentHistory> = mutableListOf()

    // Perform a trial which is a collection of steps
    private fun simulateTrial() {
        for (i in 1..numCustomers) {
            mab.step()
            trialHistories.add(mab.history)
        }
    }

    fun simulate() {
        for (i in 1..numCustomers) {
            simulateTrial()
            mab.resetMab()
        }
    }
}