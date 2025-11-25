package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.directive.impl.*
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SPACE_STRING
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test directive creation")
class DirectiveTest {

    private val packageImport = "import 'package:flutter/material.dart';"

    companion object {
        private const val EMPTY_NAME_MESSAGE = "The path of an directive can't be empty"
        private const val INVALID_CAST_USAGE =
            "The castType and importCast must be set together or must be null. A mixed state is not allowed"

        @JvmStatic
        private fun directivesWhichThrowsException() = Stream.of(
            Arguments.of({ DartDirective(SPACE_STRING) }, EMPTY_NAME_MESSAGE),
            Arguments.of({ LibraryDirective(EMPTY_STRING) }, EMPTY_NAME_MESSAGE),
            Arguments.of({ PartDirective(EMPTY_STRING) }, EMPTY_NAME_MESSAGE),
            Arguments.of({ RelativeDirective(EMPTY_STRING) }, EMPTY_NAME_MESSAGE),
            Arguments.of({ LibraryDirective(EMPTY_STRING) }, EMPTY_NAME_MESSAGE),
            Arguments.of({ DartDirective("flutter/material.dart", CastType.AS, null) }, INVALID_CAST_USAGE),
            Arguments.of({ DartDirective("flutter/material.dart", null, "test") }, INVALID_CAST_USAGE),
            Arguments.of({ RelativeDirective("flutter/material.dart", CastType.AS, null) }, INVALID_CAST_USAGE),
            Arguments.of({ RelativeDirective("flutter/material.dart", null, "test") }, INVALID_CAST_USAGE),
            Arguments.of({ ExportDirective("test.dart", CastType.HIDE, null) }, INVALID_CAST_USAGE),
            Arguments.of({ ExportDirective("test.dart", null, "test") }, INVALID_CAST_USAGE)
        )

        @JvmStatic
        private fun libDirectives() = Stream.of(
            Arguments.of(
                "library testLib;",
                DirectiveFactory.createLib("testLib"),
            ),
            Arguments.of(
                "part of testLib;",
                DirectiveFactory.createLib("testLib", true),
            ),
        )

        @JvmStatic
        private fun dartDirectives() = Stream.of(
            Arguments.of(DirectiveFactory.create(DirectiveType.IMPORT, "dart:html"), "import 'dart:html';"),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.IMPORT, "dart:http", CastType.AS, "http"),
                "import 'dart:http' as http;"
            )
        )

        @JvmStatic
        private fun exportDirectives() = Stream.of(
            Arguments.of(DirectiveFactory.create(DirectiveType.EXPORT, "test.dart"), "export 'test.dart';"),
            Arguments.of(DirectiveFactory.create(DirectiveType.EXPORT, "new_lib"), "export 'new_lib.dart';")
        )
    }

    @ParameterizedTest
    @MethodSource("directivesWhichThrowsException")
    fun `test directives which throws exception`(current: () -> Directive, expectedMessage: String) {
        val exception = assertThrows<IllegalStateException> { current() }
        assertEquals(IllegalStateException::class.java, exception.javaClass)
        assertEquals(expectedMessage, exception.message)
    }

    @ParameterizedTest(name = "Test lib directive creation for: {arguments}")
    @MethodSource("libDirectives")
    fun `test library imports`(expected: String, current: Directive, ) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @MethodSource("dartDirectives")
    fun `test dart imports`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @MethodSource("exportDirectives")
    fun `test export directive`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @EnumSource(value = CastType::class, mode = EnumSource.Mode.MATCH_ALL)
    fun `test invalid cast import mapping`(castType: CastType) {
        assertThrows<IllegalStateException> {
            DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart", castType, SPACE_STRING)
        }
    }

    @Test
    fun `test package import`() {
        val import = DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart")
        assertEquals(packageImport, import.asString())
    }
}
