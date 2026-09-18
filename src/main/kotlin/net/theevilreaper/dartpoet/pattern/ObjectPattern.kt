package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.TypeName

/**
 * An [ObjectPattern] destructures an object by calling its getters, matching each named field's
 * value against the corresponding sub-pattern (e.g. `Point(x: var px, y: var py)`).
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class ObjectPattern internal constructor(
    internal val type: TypeName,
    internal val fields: Map<String, Pattern>,
) : Pattern() {

    private val fieldEntries = fields.toMap()

    override fun toString(): String {
        val body = fieldEntries.entries.joinToString(separator = ", ") { (name, pattern) -> "$name: $pattern" }
        return "$type($body)"
    }
}
