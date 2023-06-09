package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.directive.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DirectiveTest {

    private val packageImport = "import 'package:flutter/material.dart';"

    companion object {
        private const val castValue = "item"
        private const val testImport = "../../model/item_model.dart"

        @JvmStatic
        private fun libDirectives() = Stream.of(
            Arguments.of(LibraryDirective("testLib"), "library testLib;"),
            Arguments.of(LibraryDirective("testLib", true), "part of testLib;")
        )

        @JvmStatic
        private fun dartDirectives() = Stream.of(
            Arguments.of(DartDirective("dart:html"), "import 'dart:html';"),
            Arguments.of(DartDirective("dart:http", CastType.AS, "http"), "import 'dart:http' as http;")
        )

        @JvmStatic
        private fun relativeDirectives() = Stream.of(
            Arguments.of(RelativeDirective(testImport), "import '../../model/item_model.dart';"),
            Arguments.of(RelativeDirective(testImport, CastType.AS, castValue), "import '../../model/item_model.dart' as item;"),
            Arguments.of(RelativeDirective(testImport, CastType.DEFERRED, castValue), "import '../../model/item_model.dart' deferred as item;"),
            Arguments.of(RelativeDirective(testImport, CastType.HIDE, castValue), "import '../../model/item_model.dart' hide item;"),
            Arguments.of(RelativeDirective(testImport, CastType.SHOW, castValue), "import '../../model/item_model.dart' show item;")
        )

        @JvmStatic
        private fun exportDirectives() = Stream.of(
            Arguments.of(ExportDirective("test.dart"), "export 'test.dart';"),
            Arguments.of(ExportDirective("new_lib"), "export 'new_lib.dart';")
        )
    }

    @ParameterizedTest
    @MethodSource("libDirectives")
    fun `test library imports`(current: Directive, expected: String) {
        assertEquals(expected, current.toString())
    }

    @ParameterizedTest
    @MethodSource("dartDirectives")
    fun `test dart imports`(current: Directive, expected: String) {
        assertEquals(expected, current.toString())
    }

    @ParameterizedTest
    @MethodSource("relativeDirectives")
    fun `test relative dart imports`(current: Directive, expected: String) {
        assertEquals(expected, current.toString())
    }

    @ParameterizedTest
    @MethodSource("exportDirectives")
    fun `test export directive`(current: Directive, expected: String) {
        assertEquals(expected, current.toString())
    }

    @Test
    fun `test import with empty path`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartDirective(" ") },
            "The path of an Import can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DartDirective("") },
            "The path of an Import can't be empty"
        )
    }

    @Test
    fun `test cast import with empty cast`() {
        assertThrows(
            IllegalStateException::class.java,
            { DartDirective("flutter/material.dart", CastType.AS, " ") },
            "The importCast can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DartDirective("flutter/material.dart", CastType.AS, "") },
            "The importCast can't be empty"
        )
    }


    @Test
    fun `test package import`() {
        val import = DartDirective("flutter/material.dart")
        assertEquals(packageImport, import.toString())
    }
}
