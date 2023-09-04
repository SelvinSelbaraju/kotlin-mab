package bandits.simulation

import bandits.*
import bandits.environments.Environment
import bandits.environments.MultiArmedBanditEnvironment
import bandits.strategies.*
import org.apache.commons.math3.distribution.BinomialDistribution
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.DataFrame
import utils.loadJson
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV

fun simulateWriteResults(simulators: Array<MabSimulator>): Map<String, List<Any?>> {
    val results = mutableListOf<AnyFrame>()
    for ((i,simulator) in simulators.withIndex()) {
        simulator.simulate()
        var df = mapOf(
            "trial" to simulator.mab.history.trialNumber,
            "customers" to simulator.mab.history.customers,
            "cuisine" to simulator.mab.history.armNames,
            "trueMean" to simulator.mab.history.trueMeans,
            "reward" to simulator.mab.history.rewards,
        ).toDataFrame()
        df = df.add {
            "simulation" from { i }
            "environmentName" from { simulator.mab.name }
        }
        results.add(df)
    }
    val resultsData = results.concat()

    // For each environment, take sum of each trial's rewards, take mean
    val envResults = resultsData.groupBy("environmentName", "trial")
            .sum().groupBy("environmentName").mean().getColumns("environmentName", "reward").toDataFrame()
    println("Simulation Done")
    return envResults.toMap()
}

//fun main(args: Array<String>) {
//    val environment = loadJson<Environment>("src/main/assets/environment.json")
//    val arms = environment.arms
//
//    // Strategy params to be moved to strategy specific config
//    val strategyFactory = StrategyFactory()
//    val strategy = strategyFactory.getStrategyFromConfig("src/main/assets/explore_e_greedy.json", arms)
//    val strategy2 = strategyFactory.getStrategyFromConfig("src/main/assets/no_explore_e_greedy.json", arms)
//    val strategy3 = strategyFactory.getStrategyFromConfig("src/main/assets/ts.json", arms)
//
//    val mab = MultiArmedBanditEnvironment("mab1", environment, strategy)
//    val mab2 = MultiArmedBanditEnvironment("mab2", environment, strategy2)
//    val mab3 = MultiArmedBanditEnvironment("mab3", environment, strategy3)
//
//    val simulators = arrayOf(
//        MabSimulator(mab, environment.numTrials, environment.numCustomers),
//        MabSimulator(mab2, environment.numTrials, environment.numCustomers),
//        MabSimulator(mab3, environment.numTrials, environment.numCustomers)
//    )
//
//    simulateWriteResults(simulators, args[0])
//}