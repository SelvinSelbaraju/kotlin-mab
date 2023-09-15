package bandits.simulation

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun simulateWriteResults(simulators: Array<MabSimulator>): String =
    coroutineScope {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val results = mutableListOf<AnyFrame>()
        simulators.mapIndexed() { i, simulator ->
            async(Dispatchers.Default) {
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
        }.awaitAll()
        val resultsData = results.concat()
        // For each environment, take sum of each trial's rewards, take mean
        val envResults = resultsData.groupBy("environmentName", "trial")
            .sum().groupBy("environmentName").mean().getColumns("environmentName", "reward").toDataFrame()
        resultsData.writeCSV("results.csv")
        println("Simulation Done")
        envResults.toMap().toString()
    }