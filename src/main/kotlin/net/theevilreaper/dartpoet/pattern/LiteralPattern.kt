package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.util.dartStringLiteral

/**
 * A [LiteralPattern] matches a constant value - a number, string, boolean or `null` - by equality.
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class LiteralPattern internal constructor(
    internal val value: Any?,
) : Pattern() {

    override fun toString(): String = when (value) {
        null -> "null"
        is Boolean -> value.toString()
        is Number -> value.toString()
        is String -> dartStringLiteral(value)
        else -> throw IllegalArgumentException("Unsupported literal pattern value: $value")
    }
}
