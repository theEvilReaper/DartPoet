package net.theevilreaper.dartpoet.parameter

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
    internal val modifiers = builder.specData.modifiers.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name of a parameter can't be empty" }
        check(type.trim().isNotEmpty()) { "The type can't be empty" }
    }

    fun toBuilder(): DartParameterBuilder {
        val builder = DartParameterBuilder(name, type)
        builder.named = isNamed
        builder.nullable = isNullable
        builder.required = isRequired
        return builder
    }
}