package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@DisplayName("Test identifier validation of directive aliases and library names")
class DirectiveIdentifierTest {

    companion object {

        private val invalidCastValues = listOf(
            CastType.AS to "u;\nimport 'package:evil/evil.dart'",
            CastType.AS to "my-alias",
            CastType.AS to "1alias",
            CastType.AS to "class",
            CastType.AS to "dynamic",
            CastType.DEFERRED to "lazy lib",
            CastType.SHOW to "a; import 'package:evil/evil.dart'",
            CastType.SHOW to "a,",
            CastType.HIDE to "a, b-c",
        )

        private val invalidLibraryNames = listOf(
            "foo;\nimport 'package:evil/evil.dart'",
            "my-lib",
            "dart..math",
            ".lib",
            "lib.",
            "class",
        )

        @JvmStatic
        private fun invalidCastDirectives(): Stream<Arguments> {
            val directiveTypes = listOf(DirectiveType.IMPORT, DirectiveType.RELATIVE, DirectiveType.PACKAGE, DirectiveType.EXPORT)
            return directiveTypes.flatMap { directiveType ->
                invalidCastValues
                    // Export rejects these cast types before the value is checked
                    .filterNot { directiveType == DirectiveType.EXPORT && it.first in setOf(CastType.AS, CastType.DEFERRED) }
                    .map { (castType, importCast) -> Arguments.of(directiveType, castType, importCast) }
            }.stream()
        }

        @JvmStatic
        private fun invalidLibraryDirectives(): Stream<Arguments> =
            invalidLibraryNames.flatMap { name -> listOf(false, true).map { partOf -> Arguments.of(name, partOf) } }.stream()

        @JvmStatic
        private fun validCombinators() = Stream.of(
            Arguments.of(CastType.AS, "math", "import 'dart:math' as math;"),
            Arguments.of(CastType.DEFERRED, "math", "import 'dart:math' deferred as math;"),
            Arguments.of(CastType.SHOW, "max, min", "import 'dart:math' show max, min;"),
            Arguments.of(CastType.HIDE, "Random", "import 'dart:math' hide Random;"),
        )
    }

    @ParameterizedTest(name = "{0} rejects {1} value: {2}")
    @MethodSource("invalidCastDirectives")
    fun `test directive rejects invalid cast value`(directiveType: DirectiveType, castType: CastType, importCast: String) {
        assertThrows<IllegalArgumentException> {
            DirectiveFactory.create(directiveType, "models/user.dart", castType, importCast)
        }
    }

    @ParameterizedTest(name = "Accepts {0} value: {1}")
    @MethodSource("validCombinators")
    fun `test import accepts valid cast value`(castType: CastType, importCast: String, expected: String) {
        val directive = DirectiveFactory.create(DirectiveType.IMPORT, "dart:math", castType, importCast)
        assertEquals(expected, directive.asString())
    }

    @ParameterizedTest(name = "Rejects library name: {0} (part of: {1})")
    @MethodSource("invalidLibraryDirectives")
    fun `test library rejects invalid name`(name: String, partOf: Boolean) {
        assertThrows<IllegalArgumentException> { DirectiveFactory.createLib(name, partOf) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["my_lib", "dart.math", "package.sub.lib"])
    fun `test library accepts dotted name`(name: String) {
        assertEquals("library $name;", DirectiveFactory.createLib(name).asString())
    }
}
