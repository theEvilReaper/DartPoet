package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.toImmutableList

class TypeDefWriter : Writeable<TypeDefSpec> {

    override fun write(spec: TypeDefSpec, writer: CodeWriter) {
        writer.emit("${DartModifier.TYPEDEF.identifier}·${spec.typeDefName}")
        if (spec.typeCasts.isNotEmpty()) {
            val typesAsString = spec.typeCasts.joinToString(separator = ",·") { it.toString() }
            writer.emitCode("<%L>", typesAsString)
        }
        writer.emit("·=·")
        writer.emitCode("%T", spec.returnType)

        if (spec.name != null) {
            writer.emitCode("·%L", spec.name)
        }

        emitParameters(spec, writer)
        writer.emit(SEMICOLON)
    }

    private fun emitParameters(spec: TypeDefSpec, codeWriter: CodeWriter) {
        if (spec.hasParameters) {
            codeWriter.emit("(")
            callParameterWrite(codeWriter, spec.normalParameter) { it.isNotEmpty() }
            if (spec.hasAdditionalParameters) {
                if (spec.hasParameters) {
                    codeWriter.emit(",")
                }
                codeWriter.emit("·{")
                val namedRequired = spec.namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()

                callParameterWrite(codeWriter, namedRequired) { it.isNotEmpty() }
                callParameterWrite(codeWriter, spec.requiredParameter) { it.isNotEmpty() }

                if (namedRequired.isNotEmpty()) {
                    codeWriter.emitCode(",·")
                }

                val test =
                    spec.namedParameter.minus(namedRequired).filter { it.isNullable || it.hasInitializer }.toImmutableList()
                callParameterWrite(codeWriter, test) { it.isNotEmpty() }
                codeWriter.emit("}")
            }

            if (spec.parametersWithDefaults.isNotEmpty()) {
                if (spec.hasParameters) {
                    codeWriter.emit(", ")
                }
                codeWriter.emit("[")
                callParameterWrite(codeWriter, spec.parametersWithDefaults) { it.isNotEmpty() }
                codeWriter.emit("]")
            }

            codeWriter.emit(")")
        }
    }

    private inline fun callParameterWrite(
        writer: CodeWriter,
        parameters: List<ParameterSpec>,
        crossinline predicate: (List<ParameterSpec>) -> Boolean,
    ) {
        if (!predicate.invoke(parameters)) return
        parameters.emitParameters(writer, forceNewLines = false, emitSpace = parameters.size > 1)
    }
}
