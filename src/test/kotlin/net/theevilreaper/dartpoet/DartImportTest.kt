package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.import.ImportCastType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DartImportTest {

    private val packageImport = "import 'package:flutter/material.dart';"
    private val modelImport = "import '../../model/item_model.dart' as item;"
    private val lazyCastImport = "import '../../model/item_model.dart' deferred as item;"
    private val hideCastImport = "import '../../model/item_model.dart' hide item;"
    private val showCastImport = "import '../../model/item_model.dart' show item;"
    private val dartImport = "import 'dart:html';"

    private val castValue = "item"
    private val testImport = "../../model/item_model.dart"

    @Test
    fun `test package import`() {
        val import = DartImport("flutter/material.dart")
        assertEquals(packageImport, import.toString())
    }

    @Test
    fun `test package with cast`() {
        val import = DartImport(testImport, ImportCastType.AS, castValue)
        assertEquals(modelImport, import.toString())
    }

    @Test
    fun `test lazy import`() {
        val import = DartImport(testImport, ImportCastType.DEFERRED, castValue)
        assertEquals(lazyCastImport, import.toString())
    }

    @Test
    fun `test import with hide`() {
        val import = DartImport(testImport, ImportCastType.HIDE, castValue)
        assertEquals(hideCastImport, import.toString())
    }

    @Test
    fun `test import with show`() {
        val import = DartImport(testImport, ImportCastType.SHOW, castValue)
        assertEquals(showCastImport, import.toString())
    }

    @Test
    fun `test dart import`() {
        val import = DartImport("dart:html")
        assertEquals(dartImport, import.toString())
    }
}
