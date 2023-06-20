package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The spec class contains all relevant information about a function in dart.
 * A [FunctionWriter] instance read the data from it to write the data into the function structure from dart.
 *
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
class FunctionSpec(
    builder: FunctionBuilder
) {

    internal val name = builder.name
    internal val returnType: String? = builder.returnType
    internal val body: CodeBlock = builder.body.build()
    internal val parameters: List<DartParameterSpec> = builder.parameters.toImmutableList()
    internal val isAsync: Boolean = builder.async
    internal val annotation: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    internal var modifiers: Set<DartModifier> = builder.specData.modifiers.also {
        hasAllowedModifiers(it, ALLOWED_FUNCTION_MODIFIERS, "function")
    }.filter { it != DartModifier.PRIVATE && it != DartModifier.PUBLIC }.toImmutableSet()
    internal val isNullable: Boolean = builder.nullable
    internal val isPrivate = builder.specData.modifiers.contains(DartModifier.PRIVATE)
    internal val isTypeDef = builder.typedef
    internal val typeCast = builder.typeCast
    internal val asSetter = builder.setter
    internal val isGetter = builder.getter
    internal val isLambda = builder.lambda
    internal val docs = builder.docs
    internal val hasDocs = builder.docs.isNotEmpty()

    private val namedParameters: Set<DartParameterSpec> = if (parameters.isEmpty()) {
        setOf()
    } else {
        parameters.filter { it.isNamed }.toSet()
    }

    init {
        //check(!isTypeDef && annotation.isNotEmpty()) { "A typedef can't have annotations" }
        require(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }

        if (isGetter && asSetter) {
            throw IllegalArgumentException("The function can't be a setter and a getter twice")
        }

        if (isLambda && body.isEmpty()) {
            throw IllegalArgumentException("Lambda can only be used with a body")
        }

        //require (isFactory && returnType == null && !isNullable) { "A void function can't be nullable" }
    }

    /**
     * Calls a [FunctionWriter] to append the content from a spec object to a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        FunctionWriter().emit(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    companion object {

        /**
         * Static method to create a new instance from the [FunctionBuilder].
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = FunctionBuilder(name)
    }
}
