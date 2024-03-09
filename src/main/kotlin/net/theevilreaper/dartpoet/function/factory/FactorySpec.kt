package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FactoryWriter
import net.theevilreaper.dartpoet.function.ConstructorBase
import net.theevilreaper.dartpoet.function.ConstructorDelegation
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * The [FactorySpec] represents the factory construct from the language Dart.
 * The language allows the definition of factory constructors which works like a normal constructor but with a
 * different syntax.
 */
class FactorySpec(
    builder: FactoryBuilder
) : ConstructorBase {
    val typeName: TypeName = builder.typeName
    val isConst: Boolean = builder.const
    val isPrivate: Boolean = builder.private
    val documentation: CodeBlock = builder.documentation.build()
    val parameters: List<ParameterSpec> = builder.parameters.toImmutableList()
    val initializerBlock: CodeBlock = builder.initializerBlock.build()
    val named: String? = builder.namedString
    val hasNamedData = named.orEmpty().trim().isNotEmpty()
    val constructorDelegation: ConstructorDelegation = builder.invokeType
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { !it.isRequired && it.hasInitializer }
    internal val requiredParameter =
        ParameterFilter.filterParameter(parameters) { it.isRequired && !it.isNamed && !it.hasInitializer }
    internal val namedParameter = ParameterFilter.filterParameter(parameters) { it.isNamed }
    internal val normalParameter =
        parameters.minus(parametersWithDefaults).minus(requiredParameter).minus(namedParameter).toImmutableList()
    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameter.isNotEmpty() || namedParameter.isNotEmpty()

    init {
        check(initializerBlock.isNotEmpty()) { "The initializer block must not be empty" }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        FactoryWriter().write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
     * @author theEvilReaper
     * @since 1.0.0
     * @version 1.0.0
     */
    companion object {

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
         * @param typeName the [TypeName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun builder(typeName: TypeName) = FactoryBuilder(typeName)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
         * @param className the [ClassName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun builder(className: ClassName) = FactoryBuilder(className)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object which is const by default.
         * @param typeName the [TypeName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun constBuilder(typeName: TypeName) = FactoryBuilder(typeName, true)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object which is const by default.
         * @param className the [ClassName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun constBuilder(className: ClassName) = FactoryBuilder(className, true)
    }
}