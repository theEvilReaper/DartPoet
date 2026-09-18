package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.CONST
import net.theevilreaper.dartpoet.DartModifier.PRIVATE
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitConstants
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.code.emitOperators
import net.theevilreaper.dartpoet.code.emitProperties
import net.theevilreaper.dartpoet.code.emitTypeClause
import net.theevilreaper.dartpoet.extension.type.ExtensionTypeSpec
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.GREATER_THAN_SIGN
import net.theevilreaper.dartpoet.util.LESS_THAN_SIGN
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [ExtensionTypeWriter] contains the logic to write an [ExtensionTypeSpec] to a [CodeWriter].
 * More information about extension types can be found in the [wiki](https://dart.dev/language/extension-types) from dart.
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
internal class ExtensionTypeWriter : Writeable<ExtensionTypeSpec> {

    override fun write(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writeHeader(spec, writer)
        writeGenericArguments(spec, writer)
        writeRepresentation(spec, writer)
        writeInheritance(spec, writer)

        if (spec.hasNoContent) {
            writeEmptyBody(spec, writer)
            return
        }

        writeBody(spec, writer)
    }

    private fun writeHeader(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writer.emit("extension type")
        writer.emitSpace()

        if (spec.isConst) {
            writer.emit(CONST.identifier)
            writer.emitSpace()
        }

        writer.emit(StringHelper.ensureVariableNameWithPrivateModifier(spec.name, spec.modifiers.contains(PRIVATE)))

        if (spec.constructorName != null) {
            writer.emit(".")
            writer.emit(spec.constructorName)
        }
    }

    private fun writeGenericArguments(spec: ExtensionTypeSpec, writer: CodeWriter) {
        if (spec.genericCasts.isEmpty()) return
        val joinedGenerics = StringHelper.concatData(
            spec.genericCasts,
            prefix = LESS_THAN_SIGN,
            separator = COMMA_SEPARATOR,
            postfix = GREATER_THAN_SIGN
        ) { TypeVariableName.renderDeclaration(it) }
        writer.emitCode("%L", joinedGenerics)
    }

    private fun writeRepresentation(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writer.emit("(")
        spec.representationParameter.write(writer, writeInitializer = false)
        writer.emit(")")
    }

    private fun writeInheritance(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writer.emitSpace()
        writer.emitTypeClause("implements", spec.interfaces)
    }

    private fun writeEmptyBody(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writer.emit("$CURLY_OPEN$CURLY_CLOSE")

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    private fun writeBody(spec: ExtensionTypeSpec, writer: CodeWriter) {
        writer.emit("{$NEW_LINE")
        writer.emit(NEW_LINE)
        writer.indent()

        spec.constants.emitConstants(writer)

        if (spec.constants.isNotEmpty()) {
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
