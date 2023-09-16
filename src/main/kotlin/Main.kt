import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import bandits.environments.Environment
import bandits.strategies.StrategyFactory
import ui_components.environment.*
import ui_components.strategy.StrategyManager
import utils.loadJson

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    var environment by remember { mutableStateOf(loadJson<Environment>("src/main/assets/environment.json")) }
    var strategy by remember { mutableStateOf(StrategyFactory().getStrategyFromConfig("src/main/assets/explore_e_greedy.json", environment.arms)) }
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
        EnvironmentManager(environment) { newEnv ->
            strategy.arms = newEnv.arms
            environment = newEnv
        }
        StrategyManager(strategy) { newStrategy -> strategy = newStrategy }
    }
}