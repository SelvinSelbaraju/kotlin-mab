package ui_components.utils

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun EditButtons(isReadOnly: MutableState<Boolean>, objectList: MutableList<String>, objectText: String, onChange: (MutableList<String>) -> Unit, maxObjects: Int) {
    Button(enabled = (!isReadOnly.value && objectList.size < maxObjects),onClick = {
        val listCopy = objectList.toMutableList()
        listCopy.add("$objectText ${objectList.size + 1}")
        onChange(listCopy)
    }) {
        Text("Add $objectText")
    }
    Button(enabled = (!isReadOnly.value && objectList.size > 1), onClick = {
        val listCopy = objectList.toMutableList()
        listCopy.removeLast()
        onChange(listCopy)
    }) {
        Text("Delete $objectText")
    }
    Button(onClick = { isReadOnly.value = !isReadOnly.value  }) {
        if (isReadOnly.value) {
            Text("Edit ${objectText}s")
        } else {
            Text("Save ${objectText}s")
        }
    }
}