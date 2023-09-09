package bandits.strategies

import kotlinx.serialization.Serializable
import utils.loadJson
import kotlin.reflect.full.primaryConstructor

@Serializable
data class StrategyConfig(
    val isContextual: Boolean = true,
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
        val constructorParams = strategyConstructor.parameters.associateWith {
            parameter ->
            when (parameter.name) {
                // Generic params need to be passed directly
                "arms" -> arms
                "isContextual" -> config.isContextual
                else -> config.strategyParams.get(parameter.name)
            }
        }
        return strategyConstructor.callBy(constructorParams)
    }
}