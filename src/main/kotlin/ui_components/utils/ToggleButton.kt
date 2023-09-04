package ui_components.utils

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

// Show/Hide a Composable Function
@Composable
fun ToggleButton(defaultShow: Boolean = false, componentText: String, content: @Composable() () -> Unit) {
    var show by remember { mutableStateOf(defaultShow) }
    if (show) {
        content()
    }
    Button(onClick = { show = !show }) {
        if(show) {
            Text("Hide $componentText")
        } else {
            Text("Show $componentText")
        }
    }
}