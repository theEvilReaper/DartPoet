package net.theevilreaper.dartpoet

enum class DartModifier(
    internal val identifier: String,
    private vararg val modifiers: ModifierTarget
) {

    PRIVATE("_", ModifierTarget.FUNCTION, ModifierTarget.PROPERTY),
    LATE("late", ModifierTarget.PROPERTY),
    FINAL("final", ModifierTarget.PROPERTY);

    internal fun containsTarget(modifierTarget: ModifierTarget): Boolean {
        return modifiers.contains(modifierTarget)
    }
}