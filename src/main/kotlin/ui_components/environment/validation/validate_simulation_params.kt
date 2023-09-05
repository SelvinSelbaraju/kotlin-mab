package ui_components.environment.validation

data class ErrorSimulationParams(val numTrials: String? = null, val numSteps: String? = null)
fun validateSimulationParams(numTrials: Int, numSteps: Int, constraints: EnvironmentConstraints): ErrorSimulationParams {
    var numTrialsError: String? = null
    var numStepsError: String? = null
    if (numTrials <= 0 || numTrials > constraints.maxTrials) {
        numTrialsError = "Invalid Number of Trials $numTrials"
    }
    if (numSteps <= 0 || numSteps > constraints.maxSteps) {
        numStepsError = "Invalid Number of Steps $numSteps"
    }
    return ErrorSimulationParams(numTrialsError, numStepsError)
}