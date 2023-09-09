package net.theevilreaper.dartpoet.util

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConstantsTest {

    companion object {

        @JvmStatic
        private fun patterns() = Stream.of(
            Arguments.of("", false),
            Arguments.of("Dart_FILE", false),
            Arguments.of("item_model", true),
            Arguments.of("item_model.dart", true),
            Arguments.of("model.dart", true)
        )

        @JvmStatic
        private fun camelCaseFormatting() = Stream.of(
            Arguments.of("testA", formatLowerCamelCase("testA")),
            Arguments.of("thisIsCamelCase", formatLowerCamelCase("thisisCamelcase"))
        )
    }

    @ParameterizedTest
    @MethodSource("patterns")
    fun `test name pattern`(name: String, result: Boolean) {
        if (result) {
            assertTrue(isDartConventionFileName(name))
        } else {
            assertFalse(isDartConventionFileName(name))
        }
    }

    @ParameterizedTest
    @MethodSource("camelCaseFormatting")
    fun `test camel case formatting`(expected: String, input: String) {
        assertEquals(expected, input)
    }
}
