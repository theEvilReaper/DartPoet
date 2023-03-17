package net.theevilreaper.dartpoet

import junit.framework.TestCase.assertEquals
import net.theevilreaper.dartpoet.import.DartImport
import org.junit.Test

class DartImportTest {

    private val packageImport = "'package:flutter/material.dart';"
    private val modelImport = "'../../model/item_model.dart' as item;";

    @Test
    fun `test package import`() {
        val import = DartImport("flutter/material.dart")
        assertEquals(packageImport, import.toString())
    }

    @Test
    fun `test package import with cast`() {
        val import = DartImport("../../model/item_model.dart", "item")
        println(import.toString())
        assertEquals(modelImport, import.toString())
    }
}
