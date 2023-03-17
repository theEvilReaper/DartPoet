package net.theevilreaper.dartpoet.import

import java.lang.IllegalStateException

/**
 * The class represents a normal import from dart.
 * An import statement can look like this in dart:
 * <ol>
 *  <li>'package:flutter/material.dart'</li>
 *  <li>'../../model/item_model.dart'<li>
 * </ol>
 * Dart also allows to add a prefix to an import which means that an import can look like that:
 * -> import ../../model/item_model.dart as itemModel;
 *
 * @author theEvilReaper
 * @since 1.0.0
 */
class DartImport internal constructor(
    private val path: String,
    private val importCastType: ImportCastType? = null,
    private val importCast: String? = null
) : Import {

    private val importString = buildString {
        append("import ")
        if (importCast == null && importCastType == null) {
            append("'package:$path';")
        } else if (importCast != null && importCastType != null) {
            append("'$path' ${importCastType.identifier} $importCast;")
        } else {
            throw IllegalStateException("NOPE")
        }
    }

    /**
     * Checks if the importCast string is null or empty.
     * It used to determine if the import statement should include the 'as name' in the generation
     * @return true when the string is null or empty otherwise false
     */
    private fun includePrefix(): Boolean {
        return !importCast.isNullOrEmpty()
    }

    /**
     * Checks if the given name starts with a dot.
     * @return true when the import starts with a dot otherwise false
     */
    private fun startWithDot(): Boolean {
        return this.path.startsWith(".")
    }

    override fun compareTo(other: Import): Int = importString.compareTo(other.toString())

    override fun toString(): String = importString
}