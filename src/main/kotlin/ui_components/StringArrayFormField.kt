package ui_components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bandits.environments.Environment
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

@Composable
fun StringArrayFormField(settings: Environment, property: KProperty1<Environment, *>, onSettingsChanged: (Environment) -> Unit) {
    val scrollState = rememberScrollState()

    val propertyName = property.name
    val propertyValue = property.get(settings)
    val mutableProperty = property as? KMutableProperty1<Environment, Any>

    val stringArray = propertyValue as Array<String>

    Text(propertyName)
    Row(modifier = Modifier.horizontalScroll(scrollState).padding(10.dp)) {
        stringArray.forEachIndexed { index, element ->
            TextField(
                value = element,
                onValueChange = { newValue ->
                    val updatedSettings = settings.copy()
                    val updatedArray = stringArray.copyOf()
                    updatedArray[index] = newValue
                    mutableProperty?.set(updatedSettings, updatedArray)
                    onSettingsChanged(updatedSettings)
                },
                label = { Text("$propertyName - $index") }
            )
        }
    }

}