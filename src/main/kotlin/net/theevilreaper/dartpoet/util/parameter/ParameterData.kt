package net.theevilreaper.dartpoet.util.parameter

import net.theevilreaper.dartpoet.parameter.ParameterSpec

data class ParameterData(
    val namedParameter: List<ParameterSpec>,
    val normalParameters: List<ParameterSpec>,
    val requiredParameters: List<ParameterSpec>,
    val parametersWithDefaults: List<ParameterSpec>,
    val hasAdditionalParameters: Boolean = requiredParameters.isNotEmpty() || namedParameter.isNotEmpty(),
    val hasParametersWithDefaults: Boolean = parametersWithDefaults.isNotEmpty(),
)
