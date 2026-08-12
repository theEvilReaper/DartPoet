package net.theevilreaper.dartpoet.operator

/**
 * Enumerates Dart's overloadable binary operators. Each one takes exactly one parameter
 * (the right-hand operand) when used in an `operator` declaration.
 * @author theEvilReaper
 * @since 2.1.0
 */
enum class BinaryOperator(
    override val symbol: String
) : DartOperator {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    INTEGER_DIVIDE("~/"),
    MODULO("%"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">="),
    EQUAL("=="),
    BITWISE_AND("&"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    UNSIGNED_RIGHT_SHIFT(">>>")
}