package ui_components

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import bandits.environments.Environment
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.starProjectedType

// Form Field for Int, Double or String
@Composable
fun SimpleFormField(settings: Environment, property: KProperty1<Environment, *>, onSettingsChanged: (Environment) -> Unit) {
    val propertyName = property.name
    val propertyValue = property.get(settings)
    val mutableProperty = property as? KMutableProperty1<Environment, Any>

    TextField(
        value = propertyValue.toString(),
        onValueChange = { newValue ->
            val updatedSettings = settings.copy()
            val convertedValue = when (property.returnType) {
                Int::class.starProjectedType -> newValue.toIntOrNull() ?: 0
                Double::class.starProjectedType -> newValue.toDoubleOrNull() ?: 0.0
                else -> newValue
            }
            mutableProperty?.set(updatedSettings, convertedValue)
            onSettingsChanged(updatedSettings)
        },
        label = { Text(propertyName) },
    )
}