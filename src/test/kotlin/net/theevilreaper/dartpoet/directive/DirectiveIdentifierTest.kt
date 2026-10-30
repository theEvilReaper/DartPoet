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

        @JvmStatic
        private fun invalidPrefixes() = Stream.of(
            Arguments.of(CastType.AS, "u;\nimport 'package:evil/evil.dart'"),
            Arguments.of(CastType.AS, "my-alias"),
            Arguments.of(CastType.AS, "1alias"),
            Arguments.of(CastType.AS, "class"),
            Arguments.of(CastType.AS, "dynamic"),
            Arguments.of(CastType.DEFERRED, "lazy lib"),
            Arguments.of(CastType.SHOW, "a; import 'package:evil/evil.dart'"),
            Arguments.of(CastType.SHOW, "a,"),
            Arguments.of(CastType.HIDE, "a, b-c"),
        )

        @JvmStatic
        private fun validCombinators() = Stream.of(
            Arguments.of(CastType.AS, "math", "import 'dart:math' as math;"),
            Arguments.of(CastType.DEFERRED, "math", "import 'dart:math' deferred as math;"),
            Arguments.of(CastType.SHOW, "max, min", "import 'dart:math' show max, min;"),
            Arguments.of(CastType.HIDE, "Random", "import 'dart:math' hide Random;"),
        )
    }

    @ParameterizedTest(name = "Rejects {0} value: {1}")
    @MethodSource("invalidPrefixes")
    fun `test import rejects invalid cast value`(castType: CastType, importCast: String) {
        assertThrows<IllegalArgumentException> {
            DirectiveFactory.create(DirectiveType.IMPORT, "dart:math", castType, importCast)
        }
    }

    @ParameterizedTest(name = "Rejects relative import {0} value: {1}")
    @MethodSource("invalidPrefixes")
    fun `test relative import rejects invalid cast value`(castType: CastType, importCast: String) {
        assertThrows<IllegalArgumentException> {
            DirectiveFactory.createRelative("models/user", castType, importCast)
        }
    }

    @ParameterizedTest(name = "Accepts {0} value: {1}")
    @MethodSource("validCombinators")
    fun `test import accepts valid cast value`(castType: CastType, importCast: String, expected: String) {
        val directive = DirectiveFactory.create(DirectiveType.IMPORT, "dart:math", castType, importCast)
        assertEquals(expected, directive.asString())
    }

    @ParameterizedTest
    @ValueSource(strings = ["a, b-c", "a; export 'package:evil/evil.dart'"])
    fun `test export rejects invalid combinator names`(importCast: String) {
        assertThrows<IllegalArgumentException> {
            DirectiveFactory.create(DirectiveType.EXPORT, "garden.dart", CastType.SHOW, importCast)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["foo;\nimport 'package:evil/evil.dart'", "my-lib", "dart..math", ".lib", "lib.", "class"])
    fun `test library rejects invalid name`(name: String) {
        assertThrows<IllegalArgumentException> { DirectiveFactory.createLib(name) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["foo;\nimport 'package:evil/evil.dart'", "my-lib"])
    fun `test part of rejects invalid library name`(name: String) {
        assertThrows<IllegalArgumentException> { DirectiveFactory.createLib(name, true) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["my_lib", "dart.math", "package.sub.lib"])
    fun `test library accepts dotted name`(name: String) {
        assertEquals("library $name;", DirectiveFactory.createLib(name).asString())
    }
}
