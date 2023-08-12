import bandits.*
import bandits.strategies.*
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
    val arms = environment.arms.map{ BanditArm(it.name, it.mean, it.stdDev)}.toTypedArray()

    // Strategy params to be moved to strategy specific config
    val strategyFactory = StrategyFactory()
    val strategy = strategyFactory.getStrategyFromConfig("src/main/assets/explore_e_greedy.json", arms)
    val strategy2 = strategyFactory.getStrategyFromConfig("src/main/assets/no_explore_e_greedy.json", arms)

    val mab = MultiArmedBandit("mab1", arms, strategy)
    val mab2 = MultiArmedBandit("mab2", arms, strategy2)

    val simulators = arrayOf(
        MabSimulator(mab, environment.numTrials, environment.numSteps),
        MabSimulator(mab2, environment.numTrials, environment.numSteps)
    )

    simulateWriteResults(simulators, args[0])
}