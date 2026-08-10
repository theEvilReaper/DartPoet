package net.theevilreaper.dartpoet.operator

/**
 * Marker interface for all Dart operators that can be overloaded via `operator` declarations.
 * Implemented by [UnaryOperator], [BinaryOperator] and [IndexOperator], the only operator
 * categories Dart itself allows to be overloaded.
 * @author theEvilReaper
 * @since 2.1.0
 */
sealed interface DartOperator {

    /**
     * The Dart symbol this operator is declared with, e.g. `+` or `[]=`.
     */
    val symbol: String
}