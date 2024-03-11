package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.clazz.ClassType
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
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
        when (val type = spec.classType) {
            ClassType.CLASS, ClassType.MIXIN, ClassType.ENUM -> {
                writer.emit(type.keyword)
                writer.emit("·")
                writer.emit(if (spec.modifiers.contains(PRIVATE)) PRIVATE.identifier else EMPTY_STRING)
                if (spec.name.orEmpty().trim().isNotEmpty()) {
                    writer.emit(spec.name)
                }
            }

            ClassType.ABSTRACT -> {
                writer.emit("${type.keyword}·${ClassType.CLASS.keyword}·")
                writer.emit(if (spec.modifiers.contains(PRIVATE)) PRIVATE.identifier else EMPTY_STRING)
                writer.emit(spec.name)
            }

            else -> {
                //TODO: Check if a library class needs a header
                // A library class doesn't have any class header
            }
        }
        writer.emit("·")
    }

    private fun writeInheritance(spec: ClassSpec, writer: CodeWriter) {
        if (spec.superClass != null) {
            writer.emit("${spec.inheritKeyWord!!.identifier}·")
            writer.emitCode("%T", spec.superClass)
            writer.emit("·")
        }
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
