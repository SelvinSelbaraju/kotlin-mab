package ui_components.utils

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun EditButtons(isReadOnly: MutableState<Boolean>, objectList: MutableList<String>, objectText: String, onChange: (MutableList<String>) -> Unit) {
    Button(onClick = { if (!isReadOnly.value) {
        val listCopy = objectList.toMutableList()
        listCopy.add("New $objectText")
        onChange(listCopy)
    } }) {
        Text("Add $objectText")
    }
    Button(onClick = { if (!isReadOnly.value) {
        val listCopy = objectList.toMutableList()
        listCopy.removeLast()
        onChange(listCopy)
    } }) {
        Text("Delete $objectText")
    }
    Button(onClick = { isReadOnly.value = !isReadOnly.value  }) {
        if (isReadOnly.value) {
            Text("Edit ${objectText}s")
        } else {
            Text("Save ${objectText}s")
        }
    }
    if (isReadOnly.value) {
        Text("${objectText}s Saved")
    } else {
        Text("${objectText}s Editable")
    }
}