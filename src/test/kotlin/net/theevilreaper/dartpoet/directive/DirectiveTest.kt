package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DirectiveTest {

    private val packageImport = "import 'package:flutter/material.dart';"

    companion object {
        private const val CAST_VALUE = "item"
        private const val TEST_IMPORT = "../../model/item_model.dart"
        private const val EMPTY_NAME_MESSAGE = "The path of an directive can't be empty"
        private const val INVALID_CAST_USAGE =
            "The castType and importCast must be set together or must be null. A mixed state is not allowed"

        @JvmStatic
        private fun directivesWhichThrowsException() = Stream.of(
            Arguments.of({ DartDirective(" ") }, EMPTY_NAME_MESSAGE),
            Arguments.of({ LibraryDirective("") }, EMPTY_NAME_MESSAGE),
            Arguments.of({ PartDirective("") }, EMPTY_NAME_MESSAGE),
            Arguments.of({ RelativeDirective("") }, EMPTY_NAME_MESSAGE),
            Arguments.of({ LibraryDirective("") }, EMPTY_NAME_MESSAGE),
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
                DirectiveFactory.createLib("testLib"),
                "library testLib;"
            ),
            Arguments.of(
                DirectiveFactory.createLib("testLib", true),
                "part of testLib;"
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
        private fun relativeDirectives() = Stream.of(
            Arguments.of(RelativeDirective(TEST_IMPORT), "import '../../model/item_model.dart';"),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.AS, CAST_VALUE),
                "import '../../model/item_model.dart' as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.DEFERRED, CAST_VALUE),
                "import '../../model/item_model.dart' deferred as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.HIDE, CAST_VALUE),
                "import '../../model/item_model.dart' hide item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT),
                "import '../../model/item_model.dart';"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.AS, CAST_VALUE),
                "import '../../model/item_model.dart' as item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.DEFERRED, CAST_VALUE),
                "import '../../model/item_model.dart' deferred as item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.HIDE, CAST_VALUE),
                "import '../../model/item_model.dart' hide item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.SHOW, CAST_VALUE),
                "import '../../model/item_model.dart' show item;"
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

    @ParameterizedTest
    @MethodSource("libDirectives")
    fun `test library imports`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @MethodSource("dartDirectives")
    fun `test dart imports`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @MethodSource("relativeDirectives")
    fun `test relative dart imports`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @ParameterizedTest
    @MethodSource("exportDirectives")
    fun `test export directive`(current: Directive, expected: String) {
        assertEquals(expected, current.asString())
    }

    @Test
    fun `test cast import with empty cast`() {
        assertThrows(
            IllegalStateException::class.java,
            { DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart", CastType.AS, " ") },
            "The importCast can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart", CastType.AS, "") },
            "The importCast can't be empty"
        )
    }


    @Test
    fun `test package import`() {
        val import = DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart")
        assertEquals(packageImport, import.asString())
    }
}
