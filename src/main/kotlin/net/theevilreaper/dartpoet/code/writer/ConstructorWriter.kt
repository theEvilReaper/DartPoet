package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.DelegationWriterAdapter
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON

internal class ConstructorWriter : Writeable<ConstructorSpec>, DocumentationAppender {

    override fun write(spec: ConstructorSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)

        val modifiers = getModifiersAsString(spec.isConst, spec.modifiers)
        writer.emitCode("%L", modifiers)

        writer.emit(spec.name)

        if (spec.isNamed) {
            writer.emit(".${spec.named}")
        }

        if (!spec.hasParameters) {
            writer.emit("()$SEMICOLON")
            return
        }

        writer.emit("(")

        spec.parameters.emitParameters(writer)

        if (spec.hasNamedParameters) {
            if (spec.parameters.isNotEmpty()) {
                writer.emit(",$NEW_LINE")
            }
            writer.emit("$CURLY_OPEN")
            writer.emit(NEW_LINE)
            writer.indent()

            spec.requiredAndNamedParameters.emitParameters(writer, emitSpace = false, forceNewLines = true)

            writer.unindent()
            writer.emit(NEW_LINE)
            writer.emit("$CURLY_CLOSE")
        }


        writer.emit(")")

        DelegationWriterAdapter.appendConstructorDelegation(spec.delegation, spec.initializer.build(), writer)
    }

    private fun getModifiersAsString(isConst: Boolean, modifiers: Set<DartModifier>): String {
        return when (isConst) {
            true -> modifiers.joinToString(separator = SPACE, postfix = SPACE) { it.identifier }
            else -> if (modifiers.contains(DartModifier.CONST)) {
                "${DartModifier.CONST.identifier} "
            } else {
                EMPTY_STRING
            }
        }
    }
}
