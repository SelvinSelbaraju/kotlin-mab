package utils

import kotlinx.serialization.json.Json
import java.io.File

inline fun <reified T: Any> loadJson(inputPath: String): T {
    val jsonText = File(inputPath).readText()
    return Json.decodeFromString(jsonText)
}