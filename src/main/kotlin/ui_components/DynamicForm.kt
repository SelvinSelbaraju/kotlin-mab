import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import bandits.environments.Environment
import ui_components.SimpleFormField
import ui_components.StringArrayFormField
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@Composable
fun DynamicForm(settings: Environment, onSettingsChanged: (Environment) -> Unit) {
    val properties = Environment::class.memberProperties

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (property in properties) {
            val mutableProperty = property as? KMutableProperty1<Environment, Any>

            if (mutableProperty != null) {
                // Check if propertyType is a subtype of Collection, Array, or Map
                val isPropertyMap = property.returnType.isSubtypeOf(typeOf<Map<*, *>>())
                val isPropertyList = property.returnType.isSubtypeOf(typeOf<List<*>>())
                val isPropertyArray = property.returnType.isSubtypeOf(typeOf<Array<*>>())

                if (!isPropertyMap && !isPropertyList && !isPropertyArray) {
                    SimpleFormField(settings, property, onSettingsChanged)
                } else if (isPropertyArray) {
                    StringArrayFormField(settings, property, onSettingsChanged)
                }
            }
        }
        Text(settings.toString())
    }
}
