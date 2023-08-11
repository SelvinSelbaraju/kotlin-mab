import bandits.*
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
    println("Written results to $resultsOutputPath")
}

fun main(args: Array<String>) {
    // Constants to be moved to environment specific config
    val NUMTRIALS = 5
    val NUMSTEPS = 500

    val ARMS = arrayOf(
        BanditArm("Arm 1", 1.0, 1.0),
        BanditArm("Arm 2", 0.0, 1.0),
        BanditArm("Arm 3", -1.0, 5.0),
        BanditArm("Arm 4", 1.1, 2.0),
        BanditArm("Arm 5", 1.0, 0.1),
        BanditArm("Arm 6", 0.5, 0.2),
        BanditArm("Arm 7", 1.0, 0.3),
        BanditArm("Arm 8", 0.5, 0.2),
        BanditArm("Arm 9", 0.8, 0.4),
        BanditArm("Arm 10", -1.0, 0.1),
    )

    // Strategy params to be moved to strategy specific config
    val epsilon = 0.001
    val strategy = EpsilonGreedyStrategy(epsilon)
    val epsilon2 = 0.01
    val strategy2 = EpsilonGreedyStrategy(epsilon2)

    val mab = MultiArmedBandit("mab1", ARMS, strategy)
    val mab2 = MultiArmedBandit("mab2", ARMS, strategy2)

    val simulators = arrayOf(
        MabSimulator(mab, NUMTRIALS, NUMSTEPS),
        MabSimulator(mab2, NUMTRIALS, NUMSTEPS)
    )

    simulateWriteResults(simulators, args[0])
}