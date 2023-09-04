package ui_components.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import ui_components.utils.EditButtons

@Composable
fun ArmManger(arms: MutableList<String>, armsReadOnly: MutableState<Boolean>, onChange: (MutableList<String>) -> Unit) {
    val maxObjects = 7
    Column {
        arms.forEachIndexed { index, arm ->
            TextField(
                value = arm,
                onValueChange = { it: String ->
                    val armsCopy = arms.toMutableList()
                    armsCopy[index] = it
                    onChange(armsCopy)
                },
                label = { Text("Arm $index") },
                readOnly = armsReadOnly.value
            )
        }
        EditButtons(armsReadOnly, arms, "Arm", onChange, maxObjects = maxObjects)
    }
}