package net.theevilreaper.dartpoet

/**
 * Contains all available inherit options from the programming language dart.
 * @author theEvilReaper
 * @since 1.0.0
 */
enum class InheritKeyword(
    val identifier: String
) {
    MIXIN(DartModifier.WITH.identifier),
    EXTENDS("extends"),
    IMPLEMENTS("implements")
}
