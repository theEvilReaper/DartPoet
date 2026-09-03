package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.clazz.ClassType
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.code.emitOperators
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.type.TypeVariableName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * The [ClassWriter] contains the logic to write a [ClassSpec] to a [CodeWriter].
 * It handles every [ClassType] variant. Each variant has its own header, body and closing shape.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class ClassWriter : Writeable<ClassSpec> {

    /**
     * Writes the given [ClassSpec] to a [CodeWriter] instance.
     * Dispatches to the matching shape for the spec's [ClassSpec.classType]: anonymous class, empty body or full body.
     * @param spec the [ClassSpec] which contains all data for the class
     * @param writer the [CodeWriter] instance to append the generated code into
     */
    override fun write(spec: ClassSpec, writer: CodeWriter) {
        if (spec.isAnonymous) {
            writeAnonymousClass(spec, writer)
            return
        }

        spec.annotations.emitAnnotations(writer, inLineAnnotations = false)
        writeClassHeader(spec, writer)

        writeGenericArguments(spec, writer)
        writeInheritance(spec, writer)

        if (spec.hasNoContent) {
            writeEmptyBody(spec, writer)
            return
        }

        writeClassBody(spec, writer)
    }

    /**
     * Writes an empty `{}` body for a class which has no content to generate.
     * @param spec the [ClassSpec] which has no content
     * @param writer the [CodeWriter] to write the empty body to
     */
    private fun writeEmptyBody(spec: ClassSpec, writer: CodeWriter) {
        writer.emit("$CURLY_OPEN$CURLY_CLOSE")

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }

    /**
     * Writes the full `{ ... }` body of a class.
     * Includes enum entries, constants, properties, constructors and functions.
     * @param spec the [ClassSpec] which contains the body content
     * @param writer the [CodeWriter] to write the body to
     */
    private fun writeClassBody(spec: ClassSpec, writer: CodeWriter) {
        writer.emit("{$NEW_LINE")
        writer.emit(NEW_LINE)
        writer.indent()

        if (spec.isEnum) {
            spec.enumPropertyStack.emit(writer)

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

        spec.typeDefs.emitTypeDefs(writer)

        if (spec.typeDefs.isNotEmpty()) {
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

    /**
     * Writes the generic arguments for the class header of a class in dart.
     * @param spec the [ClassSpec] which contains the generic arguments
     * @param writer the [CodeWriter] to write the generic arguments
     */
    private fun writeGenericArguments(spec: ClassSpec, writer: CodeWriter) {
        when (spec.genericCasts.isEmpty()) {
            true -> writer.emitSpace()
            false -> {
                val joinedGenerics = StringHelper.concatData(
                    spec.genericCasts,
                    prefix = LESS_THAN_SIGN,
                    separator = COMMA_SEPARATOR,
                    postfix = GREATER_THAN_SIGN
                ) { TypeVariableName.renderDeclaration(it) }
                writer.emitCode("%L", joinedGenerics)
                writer.emitSpace()
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
        val exclusiveModifier = when (spec.classType) {
            ClassType.CLASS, ClassType.ABSTRACT -> StringHelper.createModifierString(spec.modifiers.filter { it in EXCLUSIVE_CLASS_MODIFIERS })
            ClassType.MIXIN -> if (DartModifier.BASE in spec.modifiers) "${BASE.identifier} " else EMPTY_STRING
            else -> EMPTY_STRING
        }

        val mixinModifier = if (spec.isMixinClass) "${MIXIN.identifier} " else EMPTY_STRING

        val headerPrefix = when (spec.classType) {
            ClassType.ABSTRACT -> "${ABSTRACT.identifier} $exclusiveModifier$mixinModifier${CLASS.identifier}"
            ClassType.CLASS -> "$exclusiveModifier$mixinModifier${CLASS.identifier}"
            else -> "$exclusiveModifier${spec.classType.keyword}"
        }

        writer.emitCode("%L", headerPrefix)
        writer.emitSpace()
        writer.emit(StringHelper.ensureVariableNameWithPrivateModifier(spec.name, spec.modifiers.contains(PRIVATE)))
    }



    /**
     * Writes the `extends`, `with` and `implements` clauses of a class header in that order.
     * Skips any clause the [spec] doesn't declare.
     * @param spec the [ClassSpec] which contains the super class, mixins and interfaces
     * @param writer the [CodeWriter] to write the inheritance clauses to
     */
    private fun writeInheritance(spec: ClassSpec, writer: CodeWriter) {
        if (spec.superClass != null) {
            writer.emitCode("%L", "extends")
            writer.emitSpace()
            writer.emitCode("%T", spec.superClass)
            writer.emitSpace()
        }

        if (spec.mixins.isNotEmpty()) {
            writer.emitCode("%L", "with")
            writer.emitSpace()
            val joinedMixins = StringHelper.concatData(spec.mixins, separator = COMMA_SEPARATOR) { it.toString() }
            writer.emitCode("%L", joinedMixins)
            writer.emitSpace()
        }

        if (spec.onTypes.isNotEmpty()) {
            writer.emitCode("%L", "on")
            writer.emitSpace()
            val joinedOnTypes = StringHelper.concatData(spec.onTypes, separator = COMMA_SEPARATOR) { it.toString() }
            writer.emitCode("%L", joinedOnTypes)
            writer.emitSpace()
        }

        if (spec.interfaces.isNotEmpty()) {
            writer.emitCode("%L", "implements")
            writer.emitSpace()
            val joinedInterfaces = StringHelper.concatData(spec.interfaces, separator = COMMA_SEPARATOR) { it.toString() }
            writer.emitCode("%L", joinedInterfaces)
            writer.emitSpace()
        }
    }

    /**
     * Writes each [EnumEntrySpec] in the receiver list.
     * Separates entries with a comma and a newline. Does nothing if the list is empty.
     * @param codeWriter the [CodeWriter] to write the entries to
     * @param emitBlock the block used to write a single entry (defaults to [EnumEntrySpec.write])
     */
    private fun List<EnumEntrySpec>.emit(
        codeWriter: CodeWriter,
        emitBlock: (EnumEntrySpec) -> Unit = { it.write(codeWriter) }
    ) {
        if (isEmpty()) return
        forEachIndexed { index, enumPropertySpec ->
            emitBlock(enumPropertySpec)
            if (index < size - 1) {
                codeWriter.emit(",")
                codeWriter.emit(NEW_LINE)
            }
        }
    }
}
