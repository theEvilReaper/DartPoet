package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.import.Import
import net.theevilreaper.dartpoet.import.ImportCastType
import net.theevilreaper.dartpoet.import.LibraryImport
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DartImportTest {

    private val packageImport = "import 'package:flutter/material.dart';"
    companion object {
        private const val castValue = "item"
        private const val testImport = "../../model/item_model.dart"

        @JvmStatic
        private fun libImports() = Stream.of(
            Arguments.of(LibraryImport("testLib"), "library testLib;"),
            Arguments.of(LibraryImport("testLib", true), "part of testLib;")
        )

        @JvmStatic
        private fun dartImports() = Stream.of(
            Arguments.of(DartImport("dart:html"), "import 'dart:html';"),
            Arguments.of(DartImport("dart:http", ImportCastType.AS, "http"), "import 'dart:http' as http;")
        )

        @JvmStatic
        private fun relativeImports() = Stream.of(
            Arguments.of(DartImport(testImport), "import '../../model/item_model.dart';"),
            Arguments.of(DartImport(testImport, ImportCastType.AS, castValue), "import '../../model/item_model.dart' as item;"),
            Arguments.of(DartImport(testImport, ImportCastType.DEFERRED, castValue), "import '../../model/item_model.dart' deferred as item;"),
            Arguments.of(DartImport(testImport, ImportCastType.HIDE, castValue), "import '../../model/item_model.dart' hide item;"),
            Arguments.of(DartImport(testImport, ImportCastType.SHOW, castValue), "import '../../model/item_model.dart' show item;")
        )
    }

    @ParameterizedTest
    @MethodSource("libImports")
    fun `test library imports`(current: Import, expected: String) {
        assertEquals(expected, current.toString())
    }

    @ParameterizedTest
    @MethodSource("dartImports")
    fun `test dart imports`(current: Import, expected: String) {
        assertEquals(expected, current.toString())
    }

    @ParameterizedTest
    @MethodSource("relativeImports")
    fun `test relative dart imports`(current: Import, expected: String) {
        assertEquals(expected, current.toString())
    }

    @Test
    fun `test import with empty path`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartImport(" ") },
            "The path of an Import can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DartImport("") },
            "The path of an Import can't be empty"
        )
    }

    @Test
    fun `test cast import with empty cast`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartImport("flutter/material.dart", ImportCastType.AS, " ") },
            "The importCast can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DartImport("flutter/material.dart", ImportCastType.AS, "") },
            "The importCast can't be empty"
        )
    }


    @Test
    fun `test package import`() {
        val import = DartImport("flutter/material.dart")
        assertEquals(packageImport, import.toString())
    }
}
