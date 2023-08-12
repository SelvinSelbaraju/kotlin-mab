import bandits.*
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File
import kotlinx.serialization.json.Json

fun simulateWriteResults(simulators: Array<MabSimulator>, resultsOutputPath: String) {
    for (simulator in simulators) {
        simulator.simulate()
    }
    val df = dataFrameOf(
        simulators.map { it.trialRewards.toColumn(it.mab.name) }
    )
    df.writeCSV(resultsOutputPath)
    println("Written results to $resultsOutputPath")
}

inline fun <reified T: Any> loadJson(inputPath: String): T {
    val jsonText = File(inputPath).readText()
    return Json.decodeFromString(jsonText)
}

fun main(args: Array<String>) {
    val environment = loadJson<Environment>("src/main/assets/environment.json")
    val arms = environment.arms.map{ BanditArm(it.name, it.mean, it.stdDev)}.toTypedArray()

    // Strategy params to be moved to strategy specific config
    val epsilon = 0.001
    val strategy = EpsilonGreedyStrategy(epsilon)
    val epsilon2 = 0.01
    val strategy2 = EpsilonGreedyStrategy(epsilon2)

    val mab = MultiArmedBandit("mab1", arms, strategy)
    val mab2 = MultiArmedBandit("mab2", arms, strategy2)

    val simulators = arrayOf(
        MabSimulator(mab, environment.numTrials, environment.numSteps),
        MabSimulator(mab2, environment.numTrials, environment.numSteps)
    )

    simulateWriteResults(simulators, args[0])
}