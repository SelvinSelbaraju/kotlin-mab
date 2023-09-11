package bandits.simulation

import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV

fun simulateWriteResults(simulators: Array<MabSimulator>): Map<String, List<Any?>> {
    val results = mutableListOf<AnyFrame>()
    for ((i,simulator) in simulators.withIndex()) {
        simulator.simulate()
        var df = mapOf(
            "trial" to simulator.mab.history.trialNumber,
            "timeStep" to simulator.mab.history.timeStep,
            "customer" to simulator.mab.history.customers,
            "cuisine" to simulator.mab.history.armNames,
            "trueMean" to simulator.mab.history.trueMeans,
            "reward" to simulator.mab.history.rewards,
            "epsilon" to simulator.mab.history.epsilon,
            "bestMean" to simulator.mab.history.bestMeans,
            "regret" to simulator.mab.history.regrets
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
    resultsData.writeCSV("results.csv")
    println("Simulation Done")
    return envResults.toMap()
}