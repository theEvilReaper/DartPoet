package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.clazz.ClassType
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class ClassWriter : Writeable<ClassSpec> {

    //TODO: Improve new lines after each generated code part block
    override fun write(spec: ClassSpec, writer: CodeWriter) {
        if (spec.isAnonymous) {
            writeAnonymousClass(spec, writer)
            return
        }

        spec.annotations.emitAnnotations(writer, inLineAnnotations = false)
        writeClassHeader(spec, writer)
        writeGenericArguments(spec, writer)
        //Only write {} when the class contains now content
        if (spec.hasNoContent) {
            writer.emit("$CURLY_OPEN$CURLY_CLOSE")

            if (spec.endsWithNewLine) {
                writer.emit(NEW_LINE)
            }

            return
        }

        writeInheritance(spec, writer)

        writer.emit("{$NEW_LINE")
        writer.emit(NEW_LINE)
        writer.indent()

        if (spec.isEnum) {
            spec.enumPropertyStack.emit(writer) {
                it.write(writer)
            }

            if (!spec.hasNoContent) {
                writer.emit(SEMICOLON)
                writer.emit(NEW_LINE)
            }
        }

        if (spec.isEnum && spec.enumPropertyStack.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }

        spec.constantStack.emitConstants(writer)

        if (spec.constantStack.isNotEmpty()) {
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

        writer.unindent()
        if (spec.functions.isNotEmpty()) {
            writer.emit(NEW_LINE)
        }
        writer.emit("}")

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    /**
     * Writes the generic arguments for the class header of a class in dart.
     * @param spec the [ClassSpec] which contains the generic arguments
     * @param writer the [CodeWriter] to write the generic arguments
     */
    private fun writeGenericArguments(spec: ClassSpec, writer: CodeWriter) {
        when (spec.genericCasts.isEmpty()) {
            true -> writer.emit("·")
            false -> {
                val joinedGenerics = StringHelper.concatData(
                    spec.genericCasts,
                    prefix = LESS_THAN_SIGN,
                    separator = COMMA_SEPARATOR,
                    postfix = GREATER_THAN_SIGN
                ) { it.toString() }
                writer.emitCode("%L·", joinedGenerics)
            }
        }
    }

    /**
     * The method contains the logic to write an anonymous class declaration for a [ClassSpec].
     * @param spec the [ClassSpec] which contains all data for a class
     * @param writer the [CodeWriter] to write the class declaration
     */
    private fun writeAnonymousClass(spec: ClassSpec, writer: CodeWriter) {
        spec.typeDefs.emitTypeDefs(writer)
        spec.functions.emitFunctions(writer)

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    /**
     * The method contains the logic to write the dart class declaration for a [ClassSpec].
     * @param spec the [ClassSpec] which contains all data for a class
     * @param writer the [CodeWriter] to write the class declaration
     */
    private fun writeClassHeader(spec: ClassSpec, writer: CodeWriter) {
        if (spec.classType == ClassType.LIBRARY) return
        val privateModifier: String = when (spec.modifiers.contains(PRIVATE)) {
            true -> PRIVATE.identifier
            false -> EMPTY_STRING
        }
        val abstractPart: String = when (spec.classType) {
            ClassType.ABSTRACT -> "${CLASS.identifier}·"
            else -> EMPTY_STRING
        }
        writer.emitCode("%L·", spec.classType.keyword)
        writer.emitCode("%L%L%L", abstractPart, privateModifier, spec.name)
    }

    private fun writeInheritance(spec: ClassSpec, writer: CodeWriter) {
        if (spec.superClass == null) return
        writer.emit("${spec.inheritKeyWord!!.identifier}·")
        writer.emitCode("%T·", spec.superClass)
    }

    private fun List<EnumPropertySpec>.emit(
        codeWriter: CodeWriter,
        emitBlock: (EnumPropertySpec) -> Unit = { it.write(codeWriter) }
    ) = with(codeWriter) {
        if (isNotEmpty()) {
            forEachIndexed { index, enumPropertySpec ->
                emitBlock(enumPropertySpec)
                if (index < size - 1) {
                    codeWriter.emit(",$NEW_LINE")
                }
            }
        }
    }
}
