package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.parameter.ParameterChecker
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import net.theevilreaper.dartpoet.util.toImmutableList

class FunctionTypeDefSpec(
    builder: FunctionTypeDefBuilder
) : AbstractTypeDef<FunctionTypeDefBuilder>(
    builder.name, builder.typeName, builder.typeCasts.toList()
) {
    internal val returnType: TypeName = builder.returnType

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

    override fun writeRightHandSide(writer: CodeWriter) {
        writer.emitCode("%T", returnType)
        writer.emitSpace()
        writer.emitCode("%T", Function::class.asTypeName())
        val parameterData: ParameterData<ParameterSpec> = ParameterData.fromTypeDef(this)

        if (parameterData.hasParameters) {
            ParameterHelper.writeParameters(parameterData, writer, indent = parameterData.requiredParameters.size > 1)
        }
        writer.emitCode(SEMICOLON)
    }

    override fun toBuilder(): FunctionTypeDefBuilder {
        val newBuilder = FunctionTypeDefBuilder(this.name, this.type, this.typeCasts)
        newBuilder.returnType = this.returnType
        newBuilder.parameters.addAll(this.parameters)
        return newBuilder
    }
}
