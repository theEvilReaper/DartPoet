package net.theevilreaper.dartpoet.pattern

/**
 * A [MapPattern] matches a map which contains the given keys and matches each value against the
 * corresponding sub-pattern (e.g. `{'id': int id}`). Dart doesn't allow an empty map pattern.
 *
 * @author theEvilReaper
 * @since 2.6.0
 */
class MapPattern internal constructor(
    entries: Map<*, Pattern>,
) : Pattern() {

    private val entryList = entries.toList()

    override val subPatterns: List<Pattern> get() = entryList.map { it.second }

    init {
        require(entryList.isNotEmpty()) { "A map pattern must have at least one entry" }
    }

    override fun toString(): String =
        entryList.joinToString(prefix = "{", separator = ", ", postfix = "}") { (key, pattern) ->
            "${patternConstant(key)}: $pattern"
        }
}
