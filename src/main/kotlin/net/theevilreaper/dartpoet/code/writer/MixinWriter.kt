package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.BASE
import net.theevilreaper.dartpoet.DartModifier.PRIVATE
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstants
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.code.emitGenericTypeArguments
import net.theevilreaper.dartpoet.code.emitOperators
import net.theevilreaper.dartpoet.code.emitProperties
import net.theevilreaper.dartpoet.code.emitTypeClause
import net.theevilreaper.dartpoet.code.emitTypeDefs
import net.theevilreaper.dartpoet.mixin.MixinSpec
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.GREATER_THAN_SIGN
import net.theevilreaper.dartpoet.util.LESS_THAN_SIGN
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [MixinWriter] contains the logic to write a [MixinSpec] to a [CodeWriter].
 *
 * @author theEvilReaper
 * @since 2.4.0
 */
internal class MixinWriter : Writeable<MixinSpec> {

    override fun write(spec: MixinSpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(writer, inLineAnnotations = false)
        writeMixinHeader(spec, writer)
        writeGenericArguments(spec, writer)
        writeInheritance(spec, writer)

        if (spec.hasNoContent) {
            writeEmptyBody(spec, writer)
            return
        }

        writeMixinBody(spec, writer)
    }

    private fun writeMixinHeader(spec: MixinSpec, writer: CodeWriter) {
        val prefix = if (spec.isBase) "base mixin" else "mixin"
        writer.emitCode("%L", prefix)
        writer.emitSpace()
        writer.emit(StringHelper.ensureVariableNameWithPrivateModifier(spec.name, spec.modifiers.contains(PRIVATE)))
    }

    private fun writeGenericArguments(spec: MixinSpec, writer: CodeWriter) {
        writer.emitGenericTypeArguments(spec.genericCasts)
    }

    private fun writeInheritance(spec: MixinSpec, writer: CodeWriter) {
        writer.emitTypeClause("on", spec.onTypes)
        writer.emitTypeClause("implements", spec.interfaces)
    }

    private fun writeEmptyBody(spec: MixinSpec, writer: CodeWriter) {
        writer.emit("$CURLY_OPEN$CURLY_CLOSE")

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    private fun writeMixinBody(spec: MixinSpec, writer: CodeWriter) {
        writer.emit("{$NEW_LINE")
        writer.emit(NEW_LINE)
        writer.indent()

        spec.constants.emitConstants(writer)

        if (spec.constants.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.typeDefs.emitTypeDefs(writer)

        if (spec.typeDefs.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.properties.emitProperties(writer)

        if (spec.properties.isNotEmpty()) {
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
}
