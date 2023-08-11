import bandits.*
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.writeCSV


fun main(args: Array<String>) {
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

    val epsilon = 0.001
    val strategy = EpsilonGreedyStrategy(epsilon)
    val epsilon2 = 0.01
    val strategy2 = EpsilonGreedyStrategy(epsilon2)

    val mab = MultiArmedBandit("mab1", ARMS, strategy)
    val mab2 = MultiArmedBandit("mab2", ARMS, strategy2)

    val simulator1 = MabSimulator(mab, NUMTRIALS, NUMSTEPS)
    val simulator2 = MabSimulator(mab2, NUMTRIALS, NUMSTEPS)

    simulator1.simulate()
    simulator2.simulate()

    val df = dataFrameOf(
        simulator1.trialRewards.toColumn(simulator1.mab.name),
        simulator2.trialRewards.toColumn(simulator2.mab.name),
    )
    df.writeCSV(args[0])
}