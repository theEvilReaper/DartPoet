package net.theevilreaper.dartpoet.operator

/**
 * Enumerates Dart's overloadable unary operators. Each one takes no parameters when used
 * in an `operator` declaration. Dart doesn't allow overloading `!` (boolean negation), so
 * only `-` (unary minus) and `~` (bitwise complement) are represented here.
 * @author theEvilReaper
 * @since 2.1.0
 */
enum class UnaryOperator(
    override val symbol: String
) : DartOperator {
    NEGATE("-"),
    COMPLEMENT("~");
}
