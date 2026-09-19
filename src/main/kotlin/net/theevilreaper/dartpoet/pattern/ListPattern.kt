package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * A [ListPattern] destructures a Dart list by position, optionally with a rest element (`...` /
 * `...rest`) that consumes any elements between [before] and [after].
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class ListPattern internal constructor(
    internal val before: List<Pattern>,
    internal val hasRest: Boolean = false,
    internal val restName: String? = null,
    internal val after: List<Pattern> = emptyList(),
) : Pattern() {

    private val beforeList = before.toImmutableList()
    private val afterList = after.toImmutableList()

    init {
        require(restName == null || restName.trim().isNotEmpty()) {
            "The rest element's binding name can't be blank"
        }
    }

    override fun toString(): String {
        val elements = mutableListOf<String>()
        beforeList.forEach { elements += it.toString() }
        if (hasRest) {
            elements += if (restName != null) "...$restName" else "..."
        }
        afterList.forEach { elements += it.toString() }
        return elements.joinToString(prefix = "[", separator = ", ", postfix = "]")
    }
}
