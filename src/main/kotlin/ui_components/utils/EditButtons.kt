package ui_components.utils

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun EditButtons(isReadOnly: MutableState<Boolean>, objectList: MutableList<String>, objectText: String) {
    Button(onClick = { if (!isReadOnly.value) {
        objectList.add("New $objectText")
    } }) {
        Text("Add $objectText")
    }
    Button(onClick = { if (!isReadOnly.value) {
        objectList.removeLast()
    } }) {
        Text("Delete $objectText")
    }
    Button(onClick = { isReadOnly.value = !isReadOnly.value  }) {
        if (isReadOnly.value) {
            Text("Save ${objectText}s")
        } else {
            Text("Edit ${objectText}s")
        }
    }
    if (isReadOnly.value) {
        Text("${objectText}s Saved")
    } else {
        Text("${objectText}s Editable")
    }
}