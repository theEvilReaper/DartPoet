package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.DartModifier

/**
 * The interface defines some methods to add [DartModifier] instances to a class which implements the interface.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface ModifierMethods<T> {

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    fun modifier(modifier: DartModifier): T

    /**
     * Add a new [DartModifier].
     * @param modifier the modifier to add
     */
    fun modifier(modifier: () -> DartModifier): T

    /**
     * Add a variable number of [DartModifier]'s.
     * @param modifiers the modifiers to add
     */
    fun modifiers(vararg modifiers: DartModifier): T
}
