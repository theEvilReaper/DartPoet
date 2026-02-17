package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.parameter.ParameterChecker
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import net.theevilreaper.dartpoet.parameter.ParameterContext

class FunctionTypeDefSpec(
    builder: FunctionTypeDefBuilder
) : AbstractTypeDef<FunctionTypeDefBuilder>(
    builder.type
), ParameterContext<ParameterSpec> by ParameterContext(builder.parameters) {
    internal val returnType: TypeName = builder.returnType

    init {
        ParameterChecker.checkOptionalParameters(parametersWithDefaults)
    }

    override fun writeRightHandSide(writer: CodeWriter) {
        writer.emitCode("%T", returnType)
        writer.emitSpace()
        writer.emitCode("%T", Function::class.asTypeName())
        val parameterData: ParameterData<ParameterSpec> = ParameterData.of(this)

        if (parameterData.hasParameters) {
            ParameterHelper.writeParameters(parameterData, writer, indent = parameterData.requiredParameters.size > 1)
        }
        writer.emitCode(SEMICOLON)
    }

    /**
     * Creates a new [FunctionTypeDefBuilder] based on this [FunctionTypeDefSpec].
     * @return the created builder
     * @see FunctionTypeDefBuilder
     */
    override fun toBuilder(): FunctionTypeDefBuilder {
        val newBuilder = FunctionTypeDefBuilder(this.type)
        newBuilder.returnType = this.returnType
        newBuilder.parameters.addAll(this.parameters)
        return newBuilder
    }
}
