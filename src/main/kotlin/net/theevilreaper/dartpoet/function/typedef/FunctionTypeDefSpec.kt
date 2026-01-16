package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterChecker
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList

class FunctionTypeDefSpec(
    builder: FunctionTypeDefBuilder
) : TypeDefSpec(builder) {

    internal val parameters = builder.parameters.toImmutableList()

    internal val optionalNamed =
        ParameterFilter.filterParameter(parameters) { it.type == ParameterType.NAMED && (it.isNullable || it.hasInitializer) }
    internal val requiredParameters = ParameterFilter.filterParameter(parameters) { it.type == ParameterType.REQUIRED }
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { it.type == ParameterType.OPTIONAL }
    internal val normalParameters =
        ParameterHelper.excludeParameters(parameters, optionalNamed, requiredParameters, parametersWithDefaults)

    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameters.isNotEmpty() || optionalNamed.isNotEmpty()

    init {
        ParameterChecker.checkOptionalParameters(parametersWithDefaults)
    }

    override fun toBuilder(): FunctionTypeDefBuilder {
        val newBuilder = FunctionTypeDefBuilder(this.typeDefName, this.typeName, *this.typeCasts)
        newBuilder.returnType = this.returnType
        newBuilder.parameters.addAll(this.parameters)
        return newBuilder
    }
}
