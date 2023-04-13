package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.DartFunctionSpec

class FunctionWriter {

    fun emit(functionSpec: DartFunctionSpec, writer: CodeWriter) {
        if (functionSpec.returnType.orEmpty().trim().isEmpty()) {
            writer.emitCode("void·")
        } else {
            writer.emitCode(functionSpec.returnType!!)
            writer.emitCode(if (functionSpec.isNullable) "?" else "")
            writer.emitCode("·")
        }

        writer.emitCode("${if (functionSpec.isPrivate) PRIVATE.identifier else ""}${functionSpec.name}")
        writer.emitCode(if (functionSpec.isAsync) ASYNC.identifier else "")
        writer.emitCode("(")

        if (functionSpec.parameters.isNotEmpty()) {

        }

        writer.emitCode(")·{\n")
        writer.indent()
        writer.emitCode(functionSpec.body.returnsWithoutLinebreak(), ensureTrailingNewline = true)
        writer.unindent()
        writer.emit("}\n")
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