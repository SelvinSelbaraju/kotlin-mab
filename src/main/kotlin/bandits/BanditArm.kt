package bandits

import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.distribution.BinomialDistribution

class BanditArm(val name: String, private val mean: Double, private val seed: Int = 42) {
    private val bernoulli = BinomialDistribution(JDKRandomGenerator(seed), 1, mean)

    fun pullArm(): Int {
        return bernoulli.sample()
    }
}


