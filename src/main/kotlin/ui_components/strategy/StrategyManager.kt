package ui_components.strategy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import bandits.strategies.AbstractStrategy
import bandits.strategies.StrategyFactory
import ui_components.utils.Dropdown

@Composable
fun StrategyManager(strategy: AbstractStrategy, onChange: (AbstractStrategy) -> Unit) {
    var strategyName by remember { mutableStateOf(StrategyFactory().strategiesMap.keys.first()) }
    Column {
        Text("Strategy Configuration")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Strategy: $strategyName")
            Dropdown(StrategyFactory().strategiesMap.keys) { newVal ->
                if (newVal != strategyName) {
                    val newStrategy = StrategyFactory().getDefaultStrategy(newVal, strategy.arms)
                    onChange(newStrategy)
                    strategyName = newVal
                }
            }
        }
        Text(strategy.toString())
    }
}