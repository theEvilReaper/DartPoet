package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.IMPORT
import net.theevilreaper.dartpoet.util.SEMICOLON
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
class DartDirective(
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
) : BaseDirective(path) {

    /**
     * Check if some conditions are false and throw an exception.
     */
    init {
        if (castType != null && importCast != null) {
            check(importCast.trim().isNotEmpty()) { "The importCast can't be empty" }
        }
    }

    override fun write(writer: CodeWriter) {
        writer.emit("$IMPORT ")
        if (importCast == null && castType == null) {
            if (isDartImport()) {
                writer.emit("'${path.ensureDartFileEnding()}'")
            } else {
                writer.emit("'")
                writer.emit("package:")
                writer.emit(path.ensureDartFileEnding())
                writer.emit("'")
            }
            writer.emit(SEMICOLON)
        } else if (importCast != null && castType != null) {
            writer.emit("'")
            if (!isDartImport()) {
             writer.emit("package:")
            }
            writer.emit("${path.ensureDartFileEnding()}' ${castType.identifier} $importCast")
            writer.emit(SEMICOLON)
        } else {
            throw IllegalStateException("")
        }
    }
}
