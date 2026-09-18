package net.theevilreaper.dartpoet.pattern

/**
 * A [RawPattern] emits [expression] verbatim. Escape hatch for Dart pattern syntax not yet modeled
 * as a dedicated [Pattern] (relational, logical, null-check/assert and cast patterns).
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class RawPattern internal constructor(
    internal val expression: String,
) : Pattern() {

    init {
        require(expression.trim().isNotEmpty()) { "A raw pattern expression can't be empty" }
    }

    override fun toString(): String = expression
}
