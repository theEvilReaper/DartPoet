package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * A [LogicalPattern] combines [patterns] with a logical-or (`||`) or logical-and (`&&`) operator.
 * Nested patterns which bind looser than the operator are wrapped in parentheses.
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class LogicalPattern internal constructor(
    internal val operator: Operator,
    patterns: List<Pattern>,
) : Pattern() {

    internal enum class Operator(internal val symbol: String, internal val precedence: Int) {
        OR("||", LOGICAL_OR_PRECEDENCE),
        AND("&&", LOGICAL_AND_PRECEDENCE),
    }

    private val patternList = patterns.toImmutableList()

    init {
        require(patternList.size >= 2) { "A logical pattern needs at least two patterns" }
    }

    override val precedence: Int get() = operator.precedence

    override fun toString(): String =
        patternList.joinToString(separator = " ${operator.symbol} ") { it.render(operator.precedence) }
}
