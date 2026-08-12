package net.theevilreaper.dartpoet.operator

/**
 * Represents Dart's index operators, `[]` and `[]=`.
 * Unlike [BinaryOperator] and [UnaryOperator] these two have a fixed, non-symmetric
 * arity: `[]` takes the index, `[]=` takes the index and the value being assigned.
 * @author theEvilReaper
 * @since 2.1.0
 */
enum class IndexOperator(
    override val symbol: String
) : DartOperator {
    INDEX("[]"),
    INDEX_ASSIGN("[]=");
}
