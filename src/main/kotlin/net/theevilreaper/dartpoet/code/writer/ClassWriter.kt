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
class ClassWriter {

    //TODO: Improve new lines after each generated code part block
    fun write(spec: ClassSpec, codeWriter: CodeWriter) {
        if (spec.isAnonymous) {
            writeAnonymousClass(spec, codeWriter)
            return
        }
        spec.typeDefStack.emitFunctions(codeWriter) {
            it.write(codeWriter)
        }

        if (spec.typeDefStack.isNotEmpty()) {
            codeWriter.emit(NEW_LINE)
        }

        spec.annotations.emitAnnotations(codeWriter, inLineAnnotations = false) {
            it.write(codeWriter)
        }
        writeClassHeader(spec, codeWriter)

        //Only write {} when the class contains now content
        if (spec.hasNoContent) {
            codeWriter.emit("$CURLY_OPEN$CURLY_CLOSE")

            if (spec.endsWithNewLine) {
                codeWriter.emit(NEW_LINE)
            }

            return
        }

        writeInheritance(spec, codeWriter)

        codeWriter.emit("{$NEW_LINE")
        codeWriter.emit(NEW_LINE)
        codeWriter.indent()

        if (spec.isEnum) {
            spec.enumPropertyStack.emit(codeWriter) {
                it.write(codeWriter)
            }

            if (!spec.hasNoContent) {
                codeWriter.emit(SEMICOLON)
                codeWriter.emit(NEW_LINE)
            }
        }

        if (spec.isEnum && spec.enumPropertyStack.isNotEmpty()) {
            codeWriter.emit(NEW_LINE)
        }

        spec.constantStack.emitProperties(codeWriter)

        if (spec.constantStack.isNotEmpty()) {
            codeWriter.emit(NEW_LINE)
        }

        spec.properties.emitProperties(codeWriter)

        if (spec.properties.isNotEmpty()) {
            codeWriter.emit(NEW_LINE)
        }

        spec.constructors.emitConstructors(codeWriter) {
            it.write(codeWriter)
        }

        if (spec.constructors.isNotEmpty() && spec.constructors.size <= 1) {
            codeWriter.emit(NEW_LINE)
        }

        spec.functions.emitFunctions(codeWriter) {
            it.write(codeWriter)
        }

        codeWriter.unindent()
        if (spec.functions.isNotEmpty()) {
            codeWriter.emit(NEW_LINE)
        }
        codeWriter.emit("}")

        if (spec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }

    private fun writeAnonymousClass(spec: ClassSpec, writer: CodeWriter) {
        spec.typeDefStack.emitFunctions(writer) {
            it.write(writer)
        }

        spec.functions.emitFunctions(writer) {
            it.write(writer)
        }

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
                    writer.emit(spec.name!!)
                }
            }
            ClassType.ABSTRACT -> {
                writer.emit("${type.keyword}·${ClassType.CLASS.keyword}·")
                writer.emit(if (spec.modifiers.contains(PRIVATE)) PRIVATE.identifier else EMPTY_STRING)
                writer.emit(spec.name!!)
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
        emitBlock: (EnumPropertySpec) -> Unit  = { it.write(codeWriter) }
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
