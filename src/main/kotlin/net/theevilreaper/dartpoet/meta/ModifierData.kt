package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier

/**
 * The class is the implementation of the [ModifierMethods] interface for builders which only need
 * to hold modifiers.
 * @author theEvilReaper
 * @since 1.0.0
 */
class ModifierData : ModifierMethods<Unit> {

    internal val modifiers: MutableSet<DartModifier> = mutableSetOf()

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    override fun modifier(modifier: DartModifier) {
        this.modifiers += modifier
    }

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    override fun modifier(modifier: () -> DartModifier) {
        this.modifiers += modifier()
    }

    /**
     * Add an array of [DartModifier] to the given [MutableSet].
     * @param modifiers the array with the modifiers
     */
    override fun modifiers(vararg modifiers: DartModifier) {
        this.modifiers += modifiers
    }
}
