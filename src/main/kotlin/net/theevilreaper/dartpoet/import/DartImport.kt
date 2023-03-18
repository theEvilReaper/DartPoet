package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.util.IMPORT
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
        append("$IMPORT ")
        if (importCast == null && importCastType == null) {
            if (isDartImport()) {
                append("'$path';")
            } else {
                append("'package:$path';")
            }
        } else if (importCast != null && importCastType != null) {
            append("'$path' ${importCastType.identifier} $importCast;")
        } else {
            throw IllegalStateException("NOPE")
        }
    }

    /**
     * Checks if a given import path starts with the word dart.
     * @return true when the path starts with the word otherwise false
     */
    private fun isDartImport(): Boolean {
        return path.startsWith("dart")
    }

    /**
     * Compares a given import string with another import.
     */
    override fun compareTo(other: Import): Int = importString.compareTo(other.toString())

    /**
     * Returns the string which contains the created import statement.
     * @return the created import
     */
    override fun toString(): String = importString
}