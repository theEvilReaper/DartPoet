package net.theevilreaper.dartpoet.pattern

/**
 * The [RelationalOperator] enum contains all operators which can be used in a relational pattern (e.g. `>= 0`).
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
enum class RelationalOperator(internal val symbol: String) {
    EQUAL("=="),
    NOT_EQUAL("!="),
    LESS("<"),
    LESS_OR_EQUAL("<="),
    GREATER(">"),
    GREATER_OR_EQUAL(">="),
}
