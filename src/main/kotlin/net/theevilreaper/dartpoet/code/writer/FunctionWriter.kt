package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.ASYNC
import net.theevilreaper.dartpoet.DartModifier.PUBLIC
import net.theevilreaper.dartpoet.DartModifier.PRIVATE
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.MethodAccessorType
import net.theevilreaper.dartpoet.type.ParameterizedTypeName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import net.theevilreaper.dartpoet.util.toImmutableSet
import java.util.concurrent.Future

/**
 * The [FunctionWriter] contains the logic to write a [FunctionSpec] to a [Appendable].
 * It tries to recreate the structure of a method in the programming language Dart.
 *
 * **Note**: It is possible that the layout can contain a wrong format sometimes.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class FunctionWriter : Writeable<FunctionSpec>, DocumentationAppender {

    override fun write(spec: FunctionSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)

        spec.annotation.emitAnnotations(codeWriter = writer)

        if (spec.hasMethodAccessorType) {
            this.writeMethodAccessorDefinition(spec, writer)
            return
        }

        val writeableModifiers = spec.modifiers.filter { it != PRIVATE && it != PUBLIC }.toImmutableSet()
        val postFix: String = getPostFix { writeableModifiers.isNotEmpty() }
        val modifierString = writeableModifiers.joinToString(separator = SPACE, postfix = postFix) { it.identifier }

        writer.emit(modifierString)

        // The fallback is used when the spec doesn't have an async case
        val parameterizedReturnType: TypeName = parameterizedReturnType(spec) ?: spec.returnType
        writer.emitCode("%T", parameterizedReturnType)
        writer.emitSpace()
        when (spec.isPrivate) {
            true -> writer.emitCode("%L%L", PRIVATE.identifier, spec.name)
            false -> writer.emit(spec.name)
        }

        if (spec.typeCast != null) {
            writer.emitGenericBlock("%T", spec.typeCast)
        }

        val parameterData = ParameterData.of(spec)
        ParameterHelper.writeParameters(parameterData, writer)
        if (spec.isAsync) {
            writer.emitSpace()
            writer.emit(ASYNC.identifier)
        }
        writeBody(spec, writer)
    }

    /**
     * Returns the postfix [String] based on a given boolean predicate.
     * If the predicate returns true it will return [SPACE] otherwise an [EMPTY_STRING].
     * **Note** This method is a internal method which is only used in this class
     * @param predicate the predicate which is used to determine the postfix
     * @return the determined postfix [String]
     */
    private inline fun getPostFix(crossinline predicate: () -> Boolean): String {
        return when (predicate()) {
            true -> SPACE
            else -> EMPTY_STRING
        }
    }

    /**
     * Returns a [ParameterizedTypeName] if the spec is async otherwise null.
     * This is required because the return value of an async function must be wrapped in a [Future].
     * @param spec The function spec to check
     * @return null or the [ParameterizedTypeName]
     */
    private fun parameterizedReturnType(spec: FunctionSpec): TypeName? {
        return when (!spec.isAsync) {
            true -> null
            else -> Future::class.parameterizedBy(spec.returnType)
        }
    }

    private fun writeMethodAccessorDefinition(spec: FunctionSpec, writer: CodeWriter) {
        if (spec.modifiers.isNotEmpty()) {
            val keywords = StringHelper.concatData(data = spec.modifiers, separator = SPACE) { it.identifier }
            writer.emit(keywords)
            writer.emitSpace()
        }
        val typeDefinition: CodeBlock = buildCodeBlock {
            when (spec.methodAccessorType!!) {
                MethodAccessorType.SETTER -> add(MethodAccessorType.SETTER.keyword)
                MethodAccessorType.GETTER -> add("%T·${MethodAccessorType.GETTER.keyword}", spec.returnType)
            }
        }
        writer.emitCode(codeBlock = typeDefinition, ensureTrailingNewline = false)
        writer.emitSpace()
        writer.emit(spec.name)
        if (spec.hasGetterAccessor) {
            writer.emitSpace()
        }

        if (spec.hasSetterAccessor) {
            val parameterData = ParameterData.of(spec)
            ParameterHelper.writeParameters(parameterData, writer)
        }

        val bracketType = when (spec.type) {
            FunctionType.STANDARD -> {
                "·$CURLY_OPEN\n"
            }

            FunctionType.SHORTEN -> "${FunctionType.SHORTEN.identifier}·"
        }

        if (spec.type == FunctionType.STANDARD) {
            writer.indent()
        }

        writer.emit(bracketType)
        writer.emitCode(spec.body, ensureTrailingNewline = false)
        if (spec.type == FunctionType.STANDARD) {
            writer.unindent()
            writer.emit("\n$CURLY_CLOSE")
        }
    }

    /**
     * Writes the body of a given [FunctionSpec] to a [CodeWriter].
     * @param spec the spec to write
     * @param writer the writer to write the body
     */
    private fun writeBody(spec: FunctionSpec, writer: CodeWriter) {
        if (spec.body.isEmpty()) {
            writer.emit(SEMICOLON)
            return
        }

        when (spec.type) {
            FunctionType.STANDARD -> {
                writer.emitSpace()
                writer.emit("{\n")
                writer.indent()
                writeMethodBody(spec, writer)
                writer.unindent()
                writer.emit("\n}")
            }

            FunctionType.SHORTEN -> {
                writer.emitSpace()
                writer.emit(FunctionType.SHORTEN.identifier)
                writer.emitSpace()
                writer.emitCode(spec.body, ensureTrailingNewline = false)
            }
        }
    }

    /**
     * Triggers the method body write for a given [FunctionSpec].
     * @param spec the spec to write
     * @param writer the writer to write the body
     */
    private fun writeMethodBody(spec: FunctionSpec, writer: CodeWriter) {
        writer.emitCode(spec.body, ensureTrailingNewline = false)
    }
}