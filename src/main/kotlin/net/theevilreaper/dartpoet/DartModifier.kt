package net.theevilreaper.dartpoet

enum class DartModifier(
    private val identifier: String,
    vararg modifiers: ModifierTarget
) {

    PUBLIC("public", ModifierTarget.CLASS),
    FINAL("final", ModifierTarget.CLASS, ModifierTarget.FUNCTION, ModifierTarget.PROPERTY)
}