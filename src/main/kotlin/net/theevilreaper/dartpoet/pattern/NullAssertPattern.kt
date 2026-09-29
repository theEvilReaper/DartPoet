package net.theevilreaper.dartpoet.pattern

/**
 * A [NullAssertPattern] throws if the matched value is null and matches it against [pattern]
 * otherwise (e.g. `var name!`).
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class NullAssertPattern internal constructor(
    internal val pattern: Pattern,
) : Pattern() {

    override val precedence: Int get() = UNARY_PRECEDENCE

    override fun toString(): String = "${pattern.render(PRIMARY_PRECEDENCE)}!"
}
