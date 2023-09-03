package bandits.simulation

import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.distribution.BinomialDistribution

class BanditArm(private val mean: Double) {
    private val bernoulli = BinomialDistribution(1, mean)
    fun pullArm(): Int {
        return bernoulli.sample()
    }
}


