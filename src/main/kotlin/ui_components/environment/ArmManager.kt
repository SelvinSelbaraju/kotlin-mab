package ui_components.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

@Composable
fun ArmManger(arms: MutableList<String>, armsReadOnly: MutableState<Boolean>) {
    Column {
        arms.forEachIndexed { index, arm ->
            TextField(
                value = arm,
                onValueChange = { it: String ->
                    arms[index] = it
                },
                label = { Text("Arm $index") },
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