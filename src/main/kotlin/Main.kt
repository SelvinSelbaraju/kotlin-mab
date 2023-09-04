import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import bandits.environments.CustomerStats
import bandits.environments.Environment
import bandits.environments.MultiArmedBanditEnvironment
import bandits.simulation.MabSimulator
import bandits.simulation.simulateWriteResults
import bandits.strategies.StrategyFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ui_components.environment.*

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    EnvironmentManager()
}