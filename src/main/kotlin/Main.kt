import bandits.*
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.columnOf
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toColumn

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
    val mabResults: MutableList<Double> = mutableListOf()
    val mab2 = MultiArmedBandit("mab2", ARMS, strategy2)
    val mab2Results: MutableList<Double> = mutableListOf()

    for (i in 1..NUMTRIALS) {
        for (j in 1..NUMSTEPS) {
            mab.step()
            mab2.step()
        }
        mabResults.add(mab.runningReward)
        mab2Results.add(mab2.runningReward)

        mab.runningReward = 0.0
        mab2.runningReward = 0.0
    }
    val df = dataFrameOf(
        mabResults.toColumn(mab.name),
        mab2Results.toColumn(mab2.name)
    )
    df.print()
}