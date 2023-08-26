package bandits.strategies

import bandits.BanditArm
import kotlinx.serialization.Serializable
import utils.loadJson
import kotlin.reflect.full.primaryConstructor

@Serializable
data class StrategyConfig(
    val strategyType: String,
    val strategyParams: Map<String, Double>
)


class StrategyFactory() {
    private val strategiesMap = mapOf(
        "e-greedy" to EpsilonGreedyStrategy::class,
        "ts" to ThompsonSamplingStrategy::class
    )
    fun getStrategyFromConfig(configPath: String, arms: Array<String>): AbstractStrategy {
        val config = loadJson<StrategyConfig>(configPath)
        val strategyConstructor = strategiesMap[config.strategyType]!!.primaryConstructor!!
        val constructorParams = strategyConstructor.parameters.associateWith { parameter -> config.strategyParams.getOrDefault(parameter.name, arms)}
        return strategyConstructor.callBy(constructorParams)
    }
}