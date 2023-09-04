package ui_components.utils

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorMessage(isError: Boolean, errorText: String) {
    if(isError) {
        Text(errorText)
    }
}