package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.DartModifier.LIBRARY
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class LibraryImport(
    private val path: String,
    private val asPartOf: Boolean = false
): Import {

    /**
     * The init block checks some conditions and throws some exceptions when they conditions are not reached.
     */
    init {
        check(path.trim().isNotEmpty()) { "The path of an LibraryImport can't be empty" }
    }

    private val libImport: String = buildString {
        if (asPartOf) {
            append("part of ")
        } else {
            append("${LIBRARY.identifier} ")
        }
        append(path)
        append(SEMICOLON)
    }

    override fun toString(): String = libImport

    override fun compareTo(other: Import): Int = libImport.compareTo(other.toString())
}