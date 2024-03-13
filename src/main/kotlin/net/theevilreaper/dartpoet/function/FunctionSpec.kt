package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The spec class contains all relevant information about a function in dart.
 * A [FunctionWriter] instance read the data from it to write the data into the function structure from dart.
 * @param builder the builder instance to retrieve the data from
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
class FunctionSpec internal constructor(
    builder: FunctionBuilder
) {
    internal val name = builder.name
    internal val returnType: TypeName? = builder.returnType
    internal val body: CodeBlock = builder.body.build()
    private val parameters: List<ParameterSpec> = builder.parameters.toImmutableList()
    internal val isAsync: Boolean = builder.async
    internal val annotation: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    internal val modifiers: Set<DartModifier> = builder.specData.modifiers.also {
        hasAllowedModifiers(it, ALLOWED_FUNCTION_MODIFIERS, "function")
    }.filter { it != DartModifier.PRIVATE && it != DartModifier.PUBLIC }.toImmutableSet()
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { !it.isRequired && it.hasInitializer }
    internal val requiredParameter =
        ParameterFilter.filterParameter(parameters) { it.isRequired && !it.isNamed && !it.hasInitializer }
    internal val namedParameter = ParameterFilter.filterParameter(parameters) { it.isNamed }
    internal val normalParameter =
        parameters.minus(parametersWithDefaults).minus(requiredParameter).minus(namedParameter).toImmutableList()
    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameter.isNotEmpty() || namedParameter.isNotEmpty()

    internal val isPrivate = builder.specData.modifiers.remove(DartModifier.PRIVATE)
    internal val typeCast = builder.typeCast
    internal val asSetter = builder.setter
    internal val isGetter = builder.getter
    internal val isLambda = builder.lambda
    internal val docs = builder.docs

    init {
        require(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }

        if (isGetter && asSetter) {
            throw IllegalArgumentException("The function can't be a setter and a getter twice")
        }

        if (isLambda && body.isEmpty()) {
            throw IllegalArgumentException("Lambda can only be used with a body")
        }

        if (requiredParameter.isNotEmpty() && parametersWithDefaults.isNotEmpty()) {
            throw IllegalArgumentException("A function can't have required and optional parameters")
        }

        //require (isFactory && returnType == null && !isNullable) { "A void function can't be nullable" }
    }

    /**
     * Calls a [FunctionWriter] to append the content from a spec object to a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        FunctionWriter().write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [FunctionBuilder] reference from an existing [FunctionSpec] object.
     * @return the created [FunctionBuilder] instance
     */
    fun toBuilder(): FunctionBuilder {
        val builder = FunctionBuilder(this.name)
        builder.returnType = this.returnType
        builder.annotations(*this.annotation.toTypedArray())
        builder.modifiers(*this.modifiers.toTypedArray())
        builder.parameters.addAll(this.parameters)
        builder.async = this.isAsync
        builder.typeCast = this.typeCast
        builder.setter = this.asSetter
        builder.getter = this.isGetter
        builder.lambda = this.isLambda
        builder.body.formatParts.addAll(this.body.formatParts)
        builder.body.args.add(this.body.args)
        builder.docs.addAll(this.docs)
        return builder
    }

    companion object {

        /**
         * Static method to create a new instance from the [FunctionBuilder].
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = FunctionBuilder(name)
    }
}
