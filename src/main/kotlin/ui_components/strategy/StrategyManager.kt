package ui_components.strategy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import bandits.strategies.StrategyFactory
import ui_components.utils.Dropdown

@Composable
fun StrategyManager() {
    var strategyName by remember { mutableStateOf(StrategyFactory().strategiesMap.keys.first()) }
    Column {
        Text("Strategy Configuration")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Strategy: $strategyName")
            Dropdown(StrategyFactory().strategiesMap.keys) { newVal -> strategyName = newVal }
        }
    }
}