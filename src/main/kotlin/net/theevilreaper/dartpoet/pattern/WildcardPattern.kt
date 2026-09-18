package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.TypeName

/**
 * A [WildcardPattern] matches any value without binding it to a name (`_`), optionally
 * constrained to a required [typeName].
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class WildcardPattern internal constructor(
    internal val typeName: TypeName? = null,
) : Pattern() {

    override fun toString(): String = when (typeName) {
        null -> "_"
        else -> "$typeName _"
    }
}
