package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.util.escapeIfNecessary

/**
 * An [IdentifierPattern] is only the [name] of a variable (e.g. `a`). In a pattern declaration, a pattern
 * assignment or a for-in loop it binds or assigns the variable, in a matching context it's a constant.
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class IdentifierPattern internal constructor(
    internal val name: String,
) : Pattern() {

    init {
        name.escapeIfNecessary()
    }

    override fun toString(): String = name
}
