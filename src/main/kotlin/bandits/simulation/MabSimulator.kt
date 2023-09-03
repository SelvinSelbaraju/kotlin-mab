package bandits.simulation

import bandits.environments.EnvironmentHistory
import bandits.environments.MultiArmedBanditEnvironment

class MabSimulator(val mab: MultiArmedBanditEnvironment, val numTrials: Int, val numCustomers: Int) {

    // Perform a trial which is a collection of steps
    private fun simulateTrial() {
        for (i in 1..numCustomers) {
            mab.step()
        }
    }

    fun simulate() {
        for (i in 1..numTrials) {
            simulateTrial()
            mab.resetMab()
        }
    }
}