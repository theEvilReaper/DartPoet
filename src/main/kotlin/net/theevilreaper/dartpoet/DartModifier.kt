package net.theevilreaper.dartpoet

/**
 * The enum contains all modifiers that exist in the programming language dart.
 * @author theEvilReaper
 * @since 1.1.0
 */
enum class DartModifier(
    internal val identifier: String,
    private vararg val modifiers: ModifierTarget
) {
    PUBLIC("", ModifierTarget.CLASS, ModifierTarget.PROPERTY, ModifierTarget.FUNCTION),
    PRIVATE("_", ModifierTarget.CLASS, ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    STATIC("static", ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    LATE("late", ModifierTarget.PROPERTY),
    FINAL("final", ModifierTarget.CLASS, ModifierTarget.PARAMETER, ModifierTarget.PROPERTY),
    WITH("with", ModifierTarget.CLASS),
    ASYNC("async", ModifierTarget.FUNCTION),
    CONST("const", ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    EXTENSION("extension", ModifierTarget.CLASS),
    ENUM("enum", ModifierTarget.CLASS),
    MIXIN("mixin", ModifierTarget.CLASS),
    ABSTRACT("abstract", ModifierTarget.CLASS),
    FACTORY("factory", ModifierTarget.FUNCTION),
    CLASS("class", ModifierTarget.CLASS),
    ON("on", ModifierTarget.CLASS),
    TYPEDEF("typedef", ModifierTarget.TYPEDEF),
    REQUIRED("required", ModifierTarget.PARAMETER),
    CO_VARIANT("covariant", ModifierTarget.PARAMETER, ModifierTarget.PROPERTY),
    SEALED("sealed", ModifierTarget.CLASS),
    BASE("base", ModifierTarget.CLASS),
    INTERFACE("interface", ModifierTarget.CLASS),
    OPERATOR("operator", ModifierTarget.FUNCTION)
    ;

    /**
     * Checks if an [ModifierTarget] is present in a specific [DartModifier].
     * @param modifierTarget the [ModifierTarget] to test
     */
    internal fun containsTarget(modifierTarget: ModifierTarget): Boolean {
        return modifiers.contains(modifierTarget)
    }
}
