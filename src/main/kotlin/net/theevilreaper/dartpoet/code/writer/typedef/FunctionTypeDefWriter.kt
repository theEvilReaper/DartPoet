package net.theevilreaper.dartpoet.code.writer.typedef

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.function.typedef.FunctionTypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.parameter.ParameterData

class FunctionTypeDefWriter : Writeable<FunctionTypeDefSpec> {

    override fun write(
        spec: FunctionTypeDefSpec,
        writer: CodeWriter
    ) {
        writer.emitSpace()
        writer.emitCode("%T", Function::class.asTypeName())
        val parameterData: ParameterData<ParameterSpec> = ParameterData.fromTypeDef(spec)

        if (parameterData.hasParameters) {
            ParameterHelper.writeParameters(parameterData, writer, indent = parameterData.requiredParameters.size > 1)
        }
    }
}