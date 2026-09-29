package net.theevilreaper.dartpoet.pattern

/**
 * A [NullCheckPattern] only matches a non-null value and matches it against [pattern] (e.g. `var name?`).
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class NullCheckPattern internal constructor(
    internal val pattern: Pattern,
) : Pattern() {

    override val precedence: Int get() = UNARY_PRECEDENCE

    override fun toString(): String = "${pattern.render(PRIMARY_PRECEDENCE)}?"
}
