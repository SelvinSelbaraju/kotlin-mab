package ui_components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bandits.environments.CustomerStats
import bandits.environments.Environment
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

@Composable
fun MapFormField(settings: Environment, property: KProperty1<Environment, Map<String, Any>>, onSettingsChanged: (Environment) -> Unit) {
    // Get the current value
    val propertyValue = property.get(settings)
    val propertyName = property.name
    val mutableProperty = property as? KMutableProperty1<Environment, Map<String, Any>>

    val mapCopy = propertyValue.toMutableMap()
    Text(propertyName)
    for (entry in mapCopy) {
        val scrollState = rememberScrollState()
        Row(modifier = Modifier.horizontalScroll(scrollState).padding(all=10.dp)) {
            val entryClass = entry.value::class
            when (entryClass) {
                Int::class.starProjectedType, Double::class.starProjectedType, String::class.starProjectedType ->
                    TextField(
                        value = entry.value.toString(),
                        onValueChange = { newValue: String ->
                            val updatedSettings = settings.copy()
                            val convertedValue = when (property.returnType) {
                                Int::class.starProjectedType -> newValue.toIntOrNull() ?: 0
                                Double::class.starProjectedType -> newValue.toDoubleOrNull() ?: 0.0
                                else -> newValue
                            }
                            mapCopy[entry.key] = convertedValue
                            mutableProperty?.set(updatedSettings, mapCopy)
                            onSettingsChanged(updatedSettings)
                        },
                        label = { Text(entry.key) },
                    )
                // This is the Map Case
                else -> {
                    Column {
                        Text(entry.key)
                        if (entry.value::class.isData) {
                            val dcObject = (entry.value as CustomerStats).copy()
                            val dcProperties = entry.value::class.memberProperties

                            for (dcProperty in dcProperties) {
                                val dcPropertyName = dcProperty.name
                                val dcPropertyValue = dcProperty.call(dcObject)
                                val dcMutableProperty = dcProperty as? KMutableProperty1<Any, Any>
                                when (dcProperty.returnType) {
                                    Int::class.starProjectedType, Double::class.starProjectedType, String::class.starProjectedType ->
                                        TextField(
                                            value = dcPropertyValue.toString(),
                                            onValueChange = { newValue: String ->
                                                val updatedSettings = settings.copy()
                                                val convertedValue = when (dcProperty.returnType) {
                                                    Int::class.starProjectedType -> newValue.toIntOrNull() ?: 0
                                                    Double::class.starProjectedType -> newValue.toDoubleOrNull() ?: 0.0
                                                    else -> newValue
                                                }
                                                dcMutableProperty?.set(dcObject, convertedValue)
                                                mapCopy[entry.key] = dcObject
                                                mutableProperty?.set(updatedSettings, mapCopy)
                                                onSettingsChanged(updatedSettings)
                                            },
                                            label = { Text(dcPropertyName) },
                                        )
                                    // Map case
                                    else -> {
                                        val dcMapPropertyCopy = (dcPropertyValue as Map<String, Any>).toMutableMap()
                                        for (mapEntry in dcMapPropertyCopy) {
                                            TextField(
                                                value = mapEntry.value.toString(),
                                                onValueChange = { newValue: String ->
                                                    val updatedSettings = settings.copy()
                                                    val convertedValue = when (mapEntry.value) {
                                                        is Int -> newValue.toIntOrNull() ?: 0
                                                        is Double -> newValue.toDoubleOrNull() ?: 0.0
                                                        else -> newValue
                                                    }
                                                    dcMapPropertyCopy[mapEntry.key] = convertedValue
                                                    mapCopy[entry.key] = dcMapPropertyCopy
                                                    mutableProperty?.set(updatedSettings, mapCopy)
                                                    onSettingsChanged(updatedSettings)
                                                },
                                                label = { Text(mapEntry.key) },
                                            )
                                        }
                                        //
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //
}