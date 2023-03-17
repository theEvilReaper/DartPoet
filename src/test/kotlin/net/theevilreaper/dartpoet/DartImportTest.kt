package net.theevilreaper.dartpoet

import junit.framework.TestCase.assertEquals
import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.import.ImportCastType
import org.junit.Test

class DartImportTest {

    private val packageImport = "import 'package:flutter/material.dart';"
    private val modelImport = "import '../../model/item_model.dart' as item;"
    private val lazyCastImport = "import '../../model/item_model.dart' deferred as item;"

    @Test
    fun `test package import`() {
        val import = DartImport("flutter/material.dart")
        assertEquals(packageImport, import.toString())
    }

    @Test
    fun `test package import with cast`() {
        val import = DartImport("../../model/item_model.dart", ImportCastType.AS,"item")
        assertEquals(modelImport, import.toString())
    }

    @Test
    fun `test lazy package import with cast`() {
        val import = DartImport("../../model/item_model.dart", ImportCastType.DEFERRED,"item")
        assertEquals(lazyCastImport, import.toString())
    }
}
