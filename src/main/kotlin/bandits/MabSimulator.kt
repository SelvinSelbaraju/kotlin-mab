package bandits

class MabSimulator(val mab: MultiArmedBandit, val numTrials: Int, val numSteps: Int) {
    var trialRewards: MutableList<Double> = mutableListOf()

    // Perform a trial which is a collection of steps
    private fun simulateTrial() {
        for (i in 1..numSteps) {
            mab.step()
        }
        trialRewards.add(mab.runningReward)
    }

    fun simulate() {
        for (i in 1..numTrials) {
            simulateTrial()
            mab.resetMab()
        }
    }
}