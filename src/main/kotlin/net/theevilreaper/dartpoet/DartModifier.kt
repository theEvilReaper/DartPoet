package net.theevilreaper.dartpoet

enum class DartModifier(
    internal val identifier: String,
    private vararg val modifiers: ModifierTarget
) {

    PUBLIC("", ModifierTarget.CLASS, ModifierTarget.PROPERTY, ModifierTarget.FUNCTION),
    PRIVATE("_", ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    STATIC("static", ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    LATE("late", ModifierTarget.PROPERTY),
    FINAL("final", ModifierTarget.PROPERTY),

    ASYNC("async", ModifierTarget.FUNCTION),
    CONST("const", ModifierTarget.PROPERTY),

    ENUM("enum", ModifierTarget.CLASS),
    MIXIN("mixin", ModifierTarget.CLASS),
    ABSTRACT("abstract", ModifierTarget.CLASS),
    FACTORY("factory", ModifierTarget.FUNCTION),
    LIBRARY("library", ModifierTarget.CLASS),

    VOID("void", ModifierTarget.INTERFACE);

    internal fun containsTarget(modifierTarget: ModifierTarget): Boolean {
        return modifiers.contains(modifierTarget)
    }
}