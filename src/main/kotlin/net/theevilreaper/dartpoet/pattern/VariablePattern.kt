package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.TypeName

/**
 * A [VariablePattern] matches any value and binds it to a variable of the given [name],
 * optionally constraining it to [typeName] and declaring it `final` via [isFinal].
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class VariablePattern internal constructor(
    internal val name: String,
    internal val typeName: TypeName? = null,
    internal val isFinal: Boolean = false,
) : Pattern() {

    init {
        require(name.trim().isNotEmpty()) { "The name of a variable pattern can't be empty" }
    }

    override fun toString(): String = when {
        typeName != null && isFinal -> "final $typeName $name"
        typeName != null -> "$typeName $name"
        isFinal -> "final $name"
        else -> "var $name"
    }
}
