import bandits.*
import bandits.environments.Environment
import bandits.environments.MultiArmedBanditEnvironment
import bandits.strategies.*
import org.apache.commons.math3.distribution.BinomialDistribution
import utils.loadJson
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV

fun simulateWriteResults(simulators: Array<MabSimulator>, resultsOutputPath: String) {
    for (simulator in simulators) {
        simulator.simulate()
    }
    val df = dataFrameOf(
        simulators.map { it.trialRewards.toColumn(it.mab.name) }
    )
    df.writeCSV(resultsOutputPath)
    println("Means: ${df.mean()}")
    println("Written results to $resultsOutputPath")
}

fun main(args: Array<String>) {
    val environment = loadJson<Environment>("src/main/assets/environment.json")
    val arms = environment.arms

    // Strategy params to be moved to strategy specific config
    val strategyFactory = StrategyFactory()
    val strategy = strategyFactory.getStrategyFromConfig("src/main/assets/explore_e_greedy.json", arms)
    val strategy2 = strategyFactory.getStrategyFromConfig("src/main/assets/no_explore_e_greedy.json", arms)
    val strategy3 = strategyFactory.getStrategyFromConfig("src/main/assets/ts.json", arms)

    val mab = MultiArmedBanditEnvironment("mab1", environment, strategy)
    val mab2 = MultiArmedBanditEnvironment("mab2", environment, strategy2)
    val mab3 = MultiArmedBanditEnvironment("mab3", environment, strategy3)

    val simulators = arrayOf(
        MabSimulator(mab, environment.numTrials, environment.numSteps),
        MabSimulator(mab2, environment.numTrials, environment.numSteps),
        MabSimulator(mab3, environment.numTrials, environment.numSteps)
    )

    simulateWriteResults(simulators, args[0])
}