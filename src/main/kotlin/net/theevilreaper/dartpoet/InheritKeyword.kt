package net.theevilreaper.dartpoet

enum class InheritKeyword(
    val identifier: String
) {
    MIXIN(DartModifier.WITH.identifier),
    EXTENDS("extends"),
    IMPLEMENTS("implements")
}