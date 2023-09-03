package ui_components.utils

// Type conversions
fun String.convertToDouble(): Double {
    return this.toDoubleOrNull() ?: 0.0
}

fun String.convertToInt(): Int {
    return this.toIntOrNull() ?: 0
}