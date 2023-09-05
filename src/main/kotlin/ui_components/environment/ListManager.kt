package ui_components.environment

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import ui_components.utils.EditButtons

@Composable
fun ListManager(objectList: MutableList<String>, objectText: String, isReadOnly: MutableState<Boolean>, maxObjects: Int = 10, onChange: (MutableList<String>) -> Unit) {
    Column {
        objectList.forEachIndexed { index, obj ->
            TextField(
                value = obj,
                onValueChange = { it: String ->
                    val objCopy = objectList.toMutableList()
                    objCopy[index] = it
                    onChange(objCopy)
                },
                label = { Text("$objectText ${index + 1}") },
                readOnly = isReadOnly.value
            )
        }
        EditButtons(isReadOnly, objectList, objectText, onChange, maxObjects)
    }
}