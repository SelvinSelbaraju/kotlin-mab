import bandits.*

fun main(args: Array<String>) {
    val NUMSTEPS = 10000

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

    val mab = MultiArmedBandit(ARMS, strategy)
    val mab2 = MultiArmedBandit(ARMS, strategy2)

    for (i in 1..NUMSTEPS) {
        mab.step()
        mab2.step()
    }
    println("Best arm: ${mab.bestArm.name}")
    println("Num Explores: ${strategy.numExplores}")
    println("Running reward: ${mab.runningReward}")

    println("Best arm: ${mab2.bestArm.name}")
    println("Num Explores: ${strategy2.numExplores}")
    println("Running reward: ${mab2.runningReward}")
}