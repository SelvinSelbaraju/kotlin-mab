import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
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
import utils.loadJson
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@OptIn(ExperimentalComposeUiApi::class)
fun main() = singleWindowApplication {
    val arms = remember { mutableStateListOf("New Arm") }
    val armsReadOnly = remember { mutableStateOf(false) }
    Column {
        arms.forEachIndexed { index, arm ->
            TextField(
                value = arm,
                onValueChange = { it: String ->
                    arms[index] = it
                },
                label = { Text("Arm $index")},
                readOnly = armsReadOnly.value
            )
        }
        Button(onClick = { if (!armsReadOnly.value) {
            arms.add("New Arm")
        } }) {
            Text("Add Arm")
        }
        Button(onClick = { if (!armsReadOnly.value) {
            arms.removeLast()
        } }) {
            Text("Delete")
        }
        Button(onClick = { armsReadOnly.value = !armsReadOnly.value }) {
            if (!armsReadOnly.value) {
                Text("Save Arms")
            } else {
                Text("Edit Arms")
            }

        }
        if (armsReadOnly.value) {
            Text("Arms Saved")
        } else {
            Text("Arms Editable")
        }
    }
}