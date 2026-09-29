package net.theevilreaper.dartpoet.pattern

/**
 * A [RelationalPattern] compares the matched value with a constant [operand] using the given
 * [operator] (e.g. `>= 0`).
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class RelationalPattern internal constructor(
    internal val operator: RelationalOperator,
    internal val operand: Any?,
) : Pattern() {

    override val precedence: Int get() = RELATIONAL_PRECEDENCE

    override fun toString(): String = "${operator.symbol} ${patternConstant(operand)}"
}
