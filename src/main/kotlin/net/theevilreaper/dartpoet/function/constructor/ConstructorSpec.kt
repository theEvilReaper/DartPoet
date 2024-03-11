package net.theevilreaper.dartpoet.function.constructor

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.function.ConstructorBase
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

class ConstructorSpec internal constructor(
    builder: ConstructorBuilder
) : ConstructorBase {

    internal val name = builder.name
    internal val named = builder.named
    internal val isNamed = named.orEmpty().trim().isNotEmpty()
    internal val isLambda = builder.lambda
    internal val initializer = builder.initializer
    internal val modifiers = builder.modifiers.toImmutableSet()
    internal val isConst = builder.const
    private val modelParameters = builder.parameters.toImmutableSet()
    internal val requiredAndNamedParameters =
        builder.parameters.filter { it.isRequired || it.isNamed }.toImmutableList()
    internal val parameters = modelParameters.minus(requiredAndNamedParameters.toSet()).toImmutableList()
    internal val hasParameters = builder.parameters.isNotEmpty()
    internal val hasNamedParameters = requiredAndNamedParameters.isNotEmpty()
    internal val docs = builder.docs.toImmutableList()

    init {
        check(name.trim().isNotEmpty()) { "The name of a constructor can't be empty" }
    }

    /**
     * Calls the write process of the [ConstructorSpec] over the [ConstructorWriter] to the given [CodeWriter].
     * @param codeWriter the writer to write the constructor
     */
    internal fun write(codeWriter: CodeWriter) {
        ConstructorWriter().write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the created string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [ConstructorBuilder] reference from an existing [ConstructorSpec] object.
     * @return the created [ConstructorBuilder] instance
     */
    fun toBuilder(): ConstructorBuilder {
        val builder = ConstructorBuilder(this.name, this.named)
        builder.lambda = this.isLambda
        builder.modifiers.addAll(this.modifiers)
        builder.parameters.addAll(this.modelParameters)

        if (this.initializer.build().isNotEmpty()) {
            builder.initializer.formatParts.addAll(this.initializer.formatParts)
            builder.initializer.args.addAll(this.initializer.args)
        }

        builder.docs.addAll(this.docs)
        return builder
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = ConstructorBuilder(name)

        @JvmStatic
        fun named(name: String, methodName: String) = ConstructorBuilder(name, methodName)

        /**
         * Creates a new [ConstructorBuilder] reference which allows the creation of a constant constructor.
         * @param name the name of the constructor
         * @return the created [ConstructorBuilder] instance
         */
        @JvmStatic
        fun const(name: String) =
            ConstructorBuilder(name, const = true, modifiers = arrayOf(DartModifier.STATIC, DartModifier.CONST))
    }
}
