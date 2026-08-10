package net.theevilreaper.dartpoet.operator

enum class UnaryOperator(
    override val symbol: String
) : DartOperator {
    NEGATE("-"),
    COMPLEMENT("~");
}
