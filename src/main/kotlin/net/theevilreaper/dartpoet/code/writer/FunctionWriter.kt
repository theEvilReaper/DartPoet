package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.util.SEMICOLON

class FunctionWriter {

    fun emit(functionSpec: DartFunctionSpec, writer: CodeWriter) {
        if (functionSpec.isTypeDef) {
            writeTypeDef(functionSpec, writer)
            return
        } else if (functionSpec.returnType.orEmpty().trim().isEmpty()) {
            if (functionSpec.isAsync) {
                writer.emit("Future<${VOID.identifier}>·")
            } else {
                writer.emit("${VOID.identifier}·")
            }
        } else {
            if (functionSpec.isAsync) {
                writer.emit("Future<")
            }
            writer.emit(functionSpec.returnType!!)
            writer.emit(if (functionSpec.isNullable) "?" else "")

            if (functionSpec.isAsync) {
                writer.emit(">")
            }
            writer.emit("·")
        }
        writer.emit("${if (functionSpec.isPrivate) PRIVATE.identifier else ""}${functionSpec.name}")

        functionSpec.parameters.emitParameters(writer) {
            it.write(writer)
        }

        if (functionSpec.isLambda) {
            writer.emit("·=>·")
            writer.emitCode(functionSpec.body.returnsWithoutLinebreak())
        } else {
            writeBody(functionSpec, writer)
        }
    }

    private fun writeBody(spec: DartFunctionSpec, writer: CodeWriter) {
        if (spec.body.isEmpty()) {
            writer.emit(SEMICOLON)
            return
        }
        if (spec.isAsync) {
            writer.emit("·${ASYNC.identifier}")
        }
        writer.emit("·{\n")
        writer.indent()
        writer.emitCode(spec.body.returnsWithoutLinebreak(), ensureTrailingNewline = true)
        writer.unindent()
        writer.emit("}")
    }

    private fun writeTypeDef(spec: DartFunctionSpec, codeWriter: CodeWriter) {
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