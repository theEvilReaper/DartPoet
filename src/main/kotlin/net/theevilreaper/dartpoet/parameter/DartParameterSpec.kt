package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ParameterWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartParameterSpec internal constructor(
    builder: DartParameterBuilder
) {

    internal val name = builder.name
    internal val type = builder.type
    internal val isNamed = builder.named
    internal val isNullable = builder.nullable
    internal val isRequired = builder.required
    internal val initializer = builder.initializer
    internal val annotations = builder.specData.annotations.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }

        if (type != null) {
            check(type.trim().isNotEmpty()) { "The type can't be empty" }
        }
    }

    fun toBuilder(): DartParameterBuilder {
        val builder = DartParameterBuilder(name, type)
        builder.named = isNamed
        builder.nullable = isNullable
        builder.required = isRequired
        return builder
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        ParameterWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this,
        )
    }


    companion object {

        /**
         * Creates a new instance from the [DartParameterBuilder] with the given name.
         * @return the created builder instance
         */
        @JvmStatic
        fun builder(name: String) = DartParameterBuilder(name)

        /**
         * Creates a new instance from the [DartParameterBuilder] with the given values.
         * @return the created builder instance
         */
        @JvmStatic
        fun builder(name: String, type: String) = DartParameterBuilder(name, type)
    }
}