package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.parameter.ParameterData

/**
 * The [ConstructorWriter] is used in the project to write the data which comes from a [ConstructorSpec] to a [Appendable].
 * It uses the design layout for constructors from the Dart programming language specification.
 *
 * **Note:**
 * The generated code may contain formatted code parts which doesn't follow the language specifications.
 * If this happens please create an issue for that.
 *
 * @version 1.0.0
 * @author theEvilReaper
 * @since 1.0.0
 */
internal class ConstructorWriter : Writeable<ConstructorSpec>, DocumentationAppender {

    /**
     * Writes the data from a [ConstructorSpec] into a [CodeWriter] instance.
     * @param spec the [ConstructorSpec] reference which contains the data
     * @param writer the [CodeWriter] instance to append the content
     */
    override fun write(spec: ConstructorSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        if (spec.modifiers.contains(DartModifier.CONST)) {
            writer.emit(DartModifier.CONST.identifier)
            writer.emitSpace()
        }

        writer.emit(spec.name)

        if (spec.isNamed) {
            writer.emitCode(".%L", spec.named)
        }

        val parameterData: ParameterData<ParameterSpec> = ParameterData.of(spec)
        ParameterHelper.writeParameters(parameterData, writer)

        val initializerBlock: CodeBlock = spec.initializer.build()

        when (spec.isLambda) {
            true -> emitLambdaPart(writer, initializerBlock)
            false -> emitStandardPart(writer, initializerBlock)
        }
    }

    /**
     * Writes the initializer [CodeBlock] with the formating of a lambda expression in Dart.
     * @param writer the [CodeWriter] instance to apply to content
     * @param initializer the [CodeBlock] which contains the content
     */
    private fun emitLambdaPart(writer: CodeWriter, initializer: CodeBlock) {
        writer.emitSpace()
        writer.emit("=>$NEW_LINE")
        writer.indent(2)
        writer.emitCode(initializer, ensureTrailingNewline = true)
        writer.unindent(2)
    }

    /**
     * Writes the initializer [CodeBlock] with the formating of the standard layout in Dart.
     * @param writer the [CodeWriter] instance to apply to content
     * @param initializer the [CodeBlock] which contains the content
     */
    private fun emitStandardPart(writer: CodeWriter, initializer: CodeBlock) = when (initializer.isEmpty()) {
        true -> writer.emit(SEMICOLON)
        false -> {
            writer.emit(":")
            writer.emitSpace()
            writer.emitCode(initializer, ensureTrailingNewline = false)
            writer.emit(SEMICOLON)
        }
    }
}

