package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.PRIVATE
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstants
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.code.emitGenericTypeArguments
import net.theevilreaper.dartpoet.code.emitOperators
import net.theevilreaper.dartpoet.code.emitProperties
import net.theevilreaper.dartpoet.code.emitTypeClause
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.EnumSpec
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.GREATER_THAN_SIGN
import net.theevilreaper.dartpoet.util.LESS_THAN_SIGN
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [EnumWriter] contains the logic to write an [EnumSpec] to a [CodeWriter].
 *
 * @version 1.0.0
 * @since 2.4.0
 * @author theEvilReaper
 */
internal class EnumWriter : Writeable<EnumSpec> {

    /**
     * Writes the given [EnumSpec] to a [CodeWriter] instance.
     *
     * @param spec the [EnumSpec] which contains all data for the enum
     * @param writer the [CodeWriter] instance to append the generated code into
     */
    override fun write(spec: EnumSpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(writer, inLineAnnotations = false)
        writeEnumHeader(spec, writer)
        writeGenericArguments(spec, writer)
        writeInheritance(spec, writer)
        writeEnumBody(spec, writer)
    }

    private fun writeEnumHeader(spec: EnumSpec, writer: CodeWriter) {
        writer.emitCode("%L", "enum")
        writer.emitSpace()
        writer.emit(StringHelper.ensureVariableNameWithPrivateModifier(spec.name, spec.modifiers.contains(PRIVATE)))
    }

    private fun writeGenericArguments(spec: EnumSpec, writer: CodeWriter) {
        writer.emitGenericTypeArguments(spec.genericCasts)
    }

    private fun writeInheritance(spec: EnumSpec, writer: CodeWriter) {
        writer.emitTypeClause("with", spec.mixins)
        writer.emitTypeClause("implements", spec.interfaces)
    }

    private fun writeEnumBody(spec: EnumSpec, writer: CodeWriter) {
        writer.emit("{$NEW_LINE")
        writer.emit(NEW_LINE)
        writer.indent()

        spec.entries.emit(writer)

        if (!spec.hasNoContent) {
            writer.emit(SEMICOLON)
            writer.emit(NEW_LINE)
        }

        if (spec.entries.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.constants.emitConstants(writer)

        if (spec.constants.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.properties.emitProperties(writer)

        if (spec.properties.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.constructors.emitConstructors(writer)

        if (spec.constructors.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.functions.emitFunctions(writer)

        if (spec.functions.isNotEmpty() && spec.operators.isNotEmpty()) {
            writer.emit(NEW_LINE)
            writer.emit(NEW_LINE)
        }
        spec.operators.emitOperators(writer)

        writer.unindent()
        if (spec.functions.isNotEmpty() || spec.operators.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }
        writer.emit("}")

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    private fun List<EnumEntrySpec>.emit(
        codeWriter: CodeWriter,
        emitBlock: (EnumEntrySpec) -> Unit = { it.write(codeWriter) }
    ) {
        if (isEmpty()) return
        forEachIndexed { index, enumEntrySpec ->
            emitBlock(enumEntrySpec)
            if (index < size - 1) {
                codeWriter.emit(",")
                codeWriter.emit(NEW_LINE)
            }
        }
    }
}
