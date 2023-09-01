import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import bandits.environments.Environment
import kotlin.reflect.full.starProjectedType
@Composable
fun DynamicForm(settings: Environment, onSettingsChanged: (Environment) -> Unit) {
    val properties = Environment::class.memberProperties

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (property in properties) {
            val propertyName = property.name
            val propertyValue = property.get(settings)
            val mutableProperty = property as? KMutableProperty1<Environment, Any>

            if (mutableProperty != null) {
                TextField(
                    value = propertyValue.toString(),
                    onValueChange = { newValue ->
                        val updatedSettings = settings.copy()
                        val convertedValue = when (property.returnType) {
                            Int::class.starProjectedType -> newValue.toIntOrNull() ?: 0
                            Double::class.starProjectedType -> newValue.toDoubleOrNull() ?: 0.0
                            else -> newValue
                        }
                        mutableProperty.set(updatedSettings, convertedValue)
                        onSettingsChanged(updatedSettings)
                    },
                    label = { Text(propertyName) },
                )
            }
        }
    }
}
