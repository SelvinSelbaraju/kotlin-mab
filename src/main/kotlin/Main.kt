import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication

data class EnvironmentSettings(
    var numTrials: Int,
    var numCustomers: Int
)

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    MaterialTheme {
        var settings by remember { mutableStateOf(EnvironmentSettings(0, 0)) }
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Text("Environment Settings")
            TextField(
                value = settings.numTrials.toString(),
                onValueChange = {
                    val input = it.toIntOrNull() ?: 0
                    settings = settings.copy(numTrials = input)
                },
                label = { Text("Num Trials") },
            )
            TextField(
                value = settings.numCustomers.toString(),
                onValueChange = {
                    val input = it.toIntOrNull() ?: 0
                    settings = settings.copy(numCustomers = input)
                },
                label = { Text("Num Customers") },
            )
        }
    }
}