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
    WITH("with", ModifierTarget.CLASS),

    ASYNC("async", ModifierTarget.FUNCTION),
    CONST("const", ModifierTarget.PROPERTY),

    EXTENSION("extension", ModifierTarget.CLASS),
    ENUM("enum", ModifierTarget.CLASS),
    MIXIN("mixin", ModifierTarget.CLASS),
    ABSTRACT("abstract", ModifierTarget.CLASS),
    FACTORY("factory", ModifierTarget.FUNCTION),
    CLASS("class", ModifierTarget.CLASS),
    LIBRARY("library", ModifierTarget.CLASS),
    ON("on", ModifierTarget.CLASS),
    TYPEDEF("typedef", ModifierTarget.TYPEDEF),
    VOID("void", ModifierTarget.INTERFACE, ModifierTarget.FUNCTION);

    internal fun containsTarget(modifierTarget: ModifierTarget): Boolean {
        return modifiers.contains(modifierTarget)
    }
}