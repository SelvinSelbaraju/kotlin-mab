import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
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
import bandits.environments.Environment
import ui_components.environment.ArmManger
import ui_components.environment.CustomerManager
import ui_components.environment.CustomerStatsManager
import utils.loadJson
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    val scrollState = rememberScrollState()
    val arms = remember { mutableStateListOf("New Arm") }
    val customers = remember { mutableStateListOf("New Customer") }
    val armsReadOnly = remember { mutableStateOf(false) }
    val customersReadOnly = remember { mutableStateOf(false) }
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row {
            ArmManger(arms, armsReadOnly)
            CustomerManager(customers, customersReadOnly)
        }
        if (armsReadOnly.value && customersReadOnly.value) {
            CustomerStatsManager(arms, customers)
        }
    }
}