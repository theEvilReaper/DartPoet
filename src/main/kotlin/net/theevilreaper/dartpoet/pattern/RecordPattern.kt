package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * A [RecordPattern] destructures a Dart record by its positional and/or named fields.
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class RecordPattern internal constructor(
    internal val positional: List<Pattern>,
    internal val named: Map<String, Pattern>,
) : Pattern() {

    private val positionalList = positional.toImmutableList()
    private val namedFields = named.toMap()

    override fun toString(): String {
        val fields = positionalList.map { it.toString() } + namedFields.map { (name, pattern) -> "$name: $pattern" }
        return fields.joinToString(prefix = "(", separator = ", ", postfix = ")")
    }
}
