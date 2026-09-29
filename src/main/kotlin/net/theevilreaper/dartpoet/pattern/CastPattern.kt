package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.TypeName

/**
 * A [CastPattern] casts the matched value to [type] before matching it against [pattern]
 * (e.g. `var x as int`).
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class CastPattern internal constructor(
    internal val pattern: Pattern,
    internal val type: TypeName,
) : Pattern() {

    override val precedence: Int get() = UNARY_PRECEDENCE

    override val subPatterns: List<Pattern> get() = listOf(pattern)

    override fun toString(): String = "${pattern.render(PRIMARY_PRECEDENCE)} as $type"
}
