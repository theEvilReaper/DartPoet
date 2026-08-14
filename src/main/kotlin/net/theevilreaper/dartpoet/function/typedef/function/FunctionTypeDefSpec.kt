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
    builder.type,
    builder.docs,
    builder.annotationData.annotations.toSet(),
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

        // A Dart function type can't declare default parameter values, those only belong on
        // the function/constructor that actually implements the signature.
        ParameterHelper.writeParameters(
            parameterData,
            writer,
            indent = parameterData.requiredParameters.size > 1,
            writeInitializers = false,
        )
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
        newBuilder.docs.addAll(this.docs)
        newBuilder.annotations(*this.annotations.toTypedArray())
        return newBuilder
    }
}
