package bandits.distributions
import java.util.Random

class DiscreteDistribution(val probs: List<Double>, val outcomes: List<String>) {
    val random = Random()
    init {
        checkProbsValid()
    }

    // Draw random string from distribution based on probs
    fun sample(): String {
        val point = random.nextDouble()
        var returnIndex = 0
        var runningProbTotal = 0.0
        for ((i, prob) in probs.withIndex()) {
            runningProbTotal += prob
            if (runningProbTotal >= point) {
                returnIndex = i
                break
            }
        }
        return outcomes[returnIndex]
    }
    private fun checkProbsValid() {
        val probSum = probs.sum()
        if (probSum != 1.0) {
            throw Exception("Probs sum to $probSum!")
        }
        for (prob in probs) {
            if (prob < 0.0 || prob > 1.0) {
                throw Exception("Prob $prob is invalid")
            }
        }
    }
}