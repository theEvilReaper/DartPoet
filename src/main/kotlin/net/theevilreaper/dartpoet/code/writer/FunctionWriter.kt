package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.code.emitSpecialParameters
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.toImmutableSet

class FunctionWriter {

    fun emit(functionSpec: FunctionSpec, writer: CodeWriter) {
        if (functionSpec.hasDocs) {
            functionSpec.docs.forEach { writer.emitDoc(it) }
        }
        if (functionSpec.isTypeDef) {
            writeTypeDef(functionSpec, writer)
            return
        }

        val writeableModifiers = functionSpec.modifiers.filter { it != PRIVATE && it != PUBLIC }.toImmutableSet()
        val modifiersAsString = if (writeableModifiers.isEmpty()) {
            EMPTY_STRING
        } else {
            writeableModifiers.joinToString(separator = SPACE, postfix = SPACE) { it.identifier }
        }

        writer.emit(modifiersAsString)

        if (functionSpec.returnType == null) {
            if (functionSpec.isAsync) {
                writer.emitCode("Future<%L>", VOID.identifier)
            } else {
                if (functionSpec.asSetter) {
                    writer.emitCode("set·")
                } else {
                    writer.emit("${VOID.identifier}·")
                }
            }
        } else {
            if (functionSpec.isAsync) {
                writer.emit("Future<")
            }
            writer.emitCode("%T", functionSpec.returnType)

            if (functionSpec.isGetter) {
                writer.emit("·get")
            }

            if (functionSpec.isAsync) {
                writer.emit(">")
            }
            writer.emit("·")
        }
        writer.emit("${if (functionSpec.isPrivate) PRIVATE.identifier else ""}${functionSpec.name}")

        if (functionSpec.typeCast != null) {
            writer.emitCode("<%T>", functionSpec.typeCast)
        }

        if (functionSpec.isGetter) {
            writer.emit("·=>·")
            writer.emitCode(functionSpec.body.returnsWithoutLinebreak(), ensureTrailingNewline = false)
        } else {
            writer.emit("(")
            functionSpec.parameters.emitParameters(writer, emitBrackets = false)

            if (functionSpec.parameters.isNotEmpty() && functionSpec.withTrailingSpaceComma) {
                writer.emit(",$SPACE")
            }

            if (functionSpec.specialParameters.isNotEmpty()) {
                functionSpec.specialParameters.emitSpecialParameters(writer, emitAsRequired = true)
            }

            if (functionSpec.defaultParameters.isNotEmpty()) {
                functionSpec.defaultParameters.emitSpecialParameters(writer, emitAsRequired = false)
            }

            writer.emit(")")
            writeBody(functionSpec, writer)
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

    private fun writeTypeDef(spec: FunctionSpec, codeWriter: CodeWriter) {
        codeWriter.emit("${TYPEDEF.identifier}·")
        codeWriter.emit("${spec.name}·")
        codeWriter.emit("=·")
        codeWriter.emit("${spec.returnType}")
        spec.parameters.emitParameters(
            codeWriter,
            emitBrackets = spec.parameters.isNotEmpty(),
            forceNewLines = false
        ) {
            it.write(codeWriter)
        }
        codeWriter.emit(SEMICOLON)
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