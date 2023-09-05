package ui_components.environment.validation

import kotlinx.serialization.Serializable

@Serializable
data class EnvironmentConstraints(val maxArms: Int, val maxCustomers: Int, val maxTrials: Int, val maxSteps: Int)