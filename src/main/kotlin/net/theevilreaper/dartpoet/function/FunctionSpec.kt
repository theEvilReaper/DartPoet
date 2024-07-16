package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import java.lang.reflect.Type
import kotlin.reflect.KClass

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
    internal val returnType: TypeName = builder.returnType
    internal val type: FunctionType = builder.type
    internal val methodAccessorType: MethodAccessorType? = builder.methodAccessorType
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
    internal val normalParameter = ParameterHelper.excludeParameters(parameters, parametersWithDefaults, requiredParameter, namedParameter)
    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameter.isNotEmpty() || namedParameter.isNotEmpty()
    internal val isPrivate = builder.specData.modifiers.remove(DartModifier.PRIVATE)
    internal val typeCast = builder.typeCast
    internal val docs = builder.docs

    internal val hasMethodAccessorType = methodAccessorType != null
    internal val hasSetterAccessor = hasMethodAccessorType && methodAccessorType == MethodAccessorType.SETTER
    internal val hasGetterAccessor = hasMethodAccessorType && methodAccessorType == MethodAccessorType.GETTER

    init {
        require(name.trim().isNotEmpty()) { "The name of a function can't be empty" }
        require(body.isEmpty() || !modifiers.contains(DartModifier.ABSTRACT)) { "An abstract method can't have a body" }
        require(!(type == FunctionType.SHORTEN && body.isEmpty())) { "Lambda can only be used with a body" }
        require(!(requiredParameter.isNotEmpty() && parametersWithDefaults.isNotEmpty())) { "A function can't have required and optional parameters" }
    }

    /**
     * Calls a [FunctionWriter] to append the content from a spec object to a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.functionWriter.write(this, codeWriter)
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
        val builder = FunctionBuilder(this.name, this.returnType)
        builder.annotations(*this.annotation.toTypedArray())
        builder.modifiers(*this.modifiers.toTypedArray())
        builder.parameters.addAll(this.parameters)
        builder.async = this.isAsync
        builder.typeCast = this.typeCast
        builder.methodAccessorType = this.methodAccessorType
        builder.body.formatParts.addAll(this.body.formatParts)
        builder.body.args.add(this.body.args)
        builder.docs.addAll(this.docs)
        return builder
    }

    /**
     * The companion object contains several factory methods to create a new instance of [FunctionSpec].
     * @since 1.0.0
     * @version 1.0.0
     * @see FunctionBuilder
     * @author theEvilReaper
     */
    companion object {

        /**
         * Creates a new instance of [FunctionBuilder] with the specified name.
         * The return type of the function is set to [Void] by default.
         * If you want to change the type use the [FunctionBuilder.returns] method.
         * @param name the name for the function
         * @return the created [FunctionBuilder] instance
         */
        @JvmStatic
        fun builder(name: String) = FunctionBuilder(name)

        /**
         * Creates a new instance of [FunctionBuilder] with the specified name and return type.
         * @param name the name for the function
         * @param returnType the return type of the function as [TypeName]
         * @return the created [FunctionBuilder] instance
         */
        @JvmStatic
        fun builder(name: String, returnType: TypeName) = FunctionBuilder(name, returnType)

        /**
         * Creates a new instance of [FunctionBuilder] with the specified name and return type.
         * @param name the name for the function
         * @param returnType the return type of the function as [ClassName]
         * @return the created [FunctionBuilder] instance
         */
        @JvmStatic
        fun builder(name: String, returnType: ClassName) = FunctionBuilder(name, returnType)

        /**
         * Creates a new instance of [FunctionBuilder] with the specified name and return type.
         * @param name the name for the function
         * @param returnType the return type of the function as [KClass]
         * @return the created [FunctionBuilder] instance
         */
        @JvmStatic
        fun builder(name: String, returnType: KClass<*>) = FunctionBuilder(name, returnType.asTypeName())

        /**
         * Creates a new instance of [FunctionBuilder] with the specified name and return type.
         * @param name the name for the function
         * @param returnType the return type of the function as [Type]
         * @return the created [FunctionBuilder] instance
         */
        @JvmStatic
        fun builder(name: String, returnType: Type) = FunctionBuilder(name, returnType.asTypeName())
    }
}
