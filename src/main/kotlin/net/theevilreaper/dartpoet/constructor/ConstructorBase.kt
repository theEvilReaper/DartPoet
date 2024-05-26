package net.theevilreaper.dartpoet.constructor

import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Marker interface to restrict the storage usage of constructor specifications
 * to defined implementations within this project.
 * This prevents issues when adding data to collections (lists, sets, maps)
 * that store constructor-related data lacking this marker interface.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
@Internal
internal interface ConstructorBase { }