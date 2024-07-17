package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.util.EMPTY_STRING

/**
 * The [FunctionType] enum defines entries used to delegate the layout generation of a function.
 *
 * @property identifier The identifier used for delegation.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
enum class FunctionType(val identifier: String) {

    /**
     * Represents the standard function layout with an empty string as the identifier.
     */
    STANDARD(EMPTY_STRING),

    /**
     * Represents a shortened (lambda) function layout with "=>" as the identifier.
     */
    SHORTEN("=>"),
}
