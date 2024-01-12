package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.toImmutableSet

internal class FunctionWriter : Writeable<FunctionSpec> {

    override fun write(spec: FunctionSpec, writer: CodeWriter) {
        if (spec.hasDocs) {
            spec.docs.forEach { writer.emitDoc(it) }
        }

        if (spec.annotation.isNotEmpty()) {
            spec.annotation.forEach { it.write(writer) }
            writer.emit(NEW_LINE)
        }

        val writeableModifiers = spec.modifiers.filter { it != PRIVATE && it != PUBLIC }.toImmutableSet()
        val modifierString = writeableModifiers.joinToString(
            separator = SPACE,
            postfix = if (writeableModifiers.isNotEmpty()) SPACE else EMPTY_STRING
        ) { it.identifier }

        writer.emit(modifierString)

        if (spec.returnType == null) {
            if (spec.isAsync) {
                writer.emitCode("Future<%L>", VOID.identifier)
            } else {
                if (spec.asSetter) {
                    writer.emitCode("set·")
                } else {
                    writer.emit("${VOID.identifier}·")
                }
            }
        } else {

            if (spec.isAsync) {
                writer.emit("Future<")
            }
            writer.emitCode("%T", spec.returnType)

            if (spec.isGetter) {
                writer.emit("·get")
            }

            if (spec.isAsync) {
                writer.emit(">")
            }
            writer.emit("·")
        }
        writer.emit("${if (spec.isPrivate) PRIVATE.identifier else ""}${spec.name}")

        if (spec.typeCast != null) {
            writer.emitCode("<%T>", spec.typeCast)
        }

        if (spec.isGetter) {
            writer.emit("·=>·")
            writer.emitCode(spec.body.returnsWithoutLinebreak(), ensureTrailingNewline = false)
        } else {
            writeParameters(spec, writer)
            writeBody(spec, writer)
        }
    }

    private fun writeBody(spec: FunctionSpec, writer: CodeWriter) {
        if (spec.body.isEmpty()) {
            writer.emit(SEMICOLON)
            return
        }
        if (spec.isAsync) {
            writer.emit("·${ASYNC.identifier}")
        }
        if (spec.isLambda) {
            writer.emit("·=>·")
        } else {
            writer.emit("·{\n")
            writer.indent()
        }
        writer.emitCode(spec.body.returnsWithoutLinebreak(), ensureTrailingNewline = false)
        if (!spec.isLambda) {
            writer.unindent()
            writer.emit("\n}")
        }
    }

    private fun writeParameters(spec: FunctionSpec, codeWriter: CodeWriter) {
        if (!spec.hasParameters && spec.isTypeDef) {
            return
        }
        codeWriter.emit("(")
        spec.normalParameter.emitParameters(codeWriter, forceNewLines = false)

        if (spec.normalParameter.isNotEmpty() && (spec.hasAdditionalParameters || spec.parametersWithDefaults.isNotEmpty())) {
            codeWriter.emit(", ")
        }

        if (spec.hasAdditionalParameters) {
            emitRequiredAndNamedParameter(spec, codeWriter)
        }

        if (spec.parametersWithDefaults.isNotEmpty()) {
            codeWriter.emit("[")
            spec.parametersWithDefaults.emitParameters(codeWriter, forceNewLines = false)
            codeWriter.emit("]")
        }

        codeWriter.emit(")")
    }

    private fun emitRequiredAndNamedParameter(spec: FunctionSpec, codeWriter: CodeWriter) {
        codeWriter.emit("$CURLY_OPEN")

        val namedRequired = spec.namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()

        writeParameters(namedRequired, spec.normalParameter.isNotEmpty(), codeWriter)
        /*if (namedRequired.isNotEmpty()) {
            namedRequired.emitParameters(codeWriter, forceNewLines = false)

            if (spec.requiredParameter.isNotEmpty()) {
                codeWriter.emit(", ")
            }
        }*/

        writeParameters(spec.requiredParameter, namedRequired.isNotEmpty(), codeWriter)
       // spec.requiredParameter.emitParameters(codeWriter, forceNewLines = false)

        val test =
            spec.namedParameter.minus(namedRequired).filter { it.isNullable || it.hasInitializer }.toImmutableList()

        writeParameters(test, spec.requiredParameter.isNotEmpty() || namedRequired.isNotEmpty(), codeWriter)
        /*if (test.isNotEmpty()) {
            codeWriter.emit(", ")
            test.emitParameters(codeWriter, forceNewLines = false)
        }*/

        codeWriter.emit("$CURLY_CLOSE")
    }

    private fun writeTypeDef(spec: FunctionSpec, codeWriter: CodeWriter) {
        codeWriter.emit("${TYPEDEF.identifier}·")
        codeWriter.emit("${spec.name}·")
        codeWriter.emit("=·")
        codeWriter.emit("${spec.returnType}")
        writeParameters(spec, codeWriter)
        codeWriter.emit(SEMICOLON)
    }

    private fun writeParameters(parameters: List<ParameterSpec>, emitSpaceComma: Boolean = false, codeWriter: CodeWriter) {
        if (parameters.isEmpty()) return
        if (emitSpaceComma) {
            codeWriter.emit(", ")
        }
        parameters.emitParameters(codeWriter, forceNewLines = false)
    }

    private val RETURN_EXPRESSION_BODY_PREFIX_SPACE = CodeBlock.of("return ")
    private val RETURN_EXPRESSION_BODY_PREFIX_NBSP = CodeBlock.of("return·")
    private val THROW_EXPRESSION_BODY_PREFIX_SPACE = CodeBlock.of("throw ")
    private val THROW_EXPRESSION_BODY_PREFIX_NBSP = CodeBlock.of("throw·")

    private fun CodeBlock.returnsWithoutLinebreak(): CodeBlock {
        val returnWithSpace = RETURN_EXPRESSION_BODY_PREFIX_SPACE.formatParts[0]
        val returnWithNbsp = RETURN_EXPRESSION_BODY_PREFIX_NBSP.formatParts[0]
        var originCodeBlockBuilder: CodeBlock.Builder? = null
        for ((i, formatPart) in formatParts.withIndex()) {
            if (formatPart.startsWith(returnWithSpace)) {
                val builder = originCodeBlockBuilder ?: toBuilder()
                originCodeBlockBuilder = builder
                builder.formatParts[i] = formatPart.replaceFirst(returnWithSpace, returnWithNbsp)
            }
        }
        return originCodeBlockBuilder?.build() ?: this
    }

    private fun CodeBlock.asExpressionBody(): CodeBlock? {
        val codeBlock = this.trim()
        val asReturnExpressionBody = codeBlock.withoutPrefix(RETURN_EXPRESSION_BODY_PREFIX_SPACE)
            ?: codeBlock.withoutPrefix(RETURN_EXPRESSION_BODY_PREFIX_NBSP)
        if (asReturnExpressionBody != null) {
            return asReturnExpressionBody
        }
        if (codeBlock.withoutPrefix(THROW_EXPRESSION_BODY_PREFIX_SPACE) != null ||
            codeBlock.withoutPrefix(THROW_EXPRESSION_BODY_PREFIX_NBSP) != null
        ) {
            return codeBlock
        }
        return null
    }
}