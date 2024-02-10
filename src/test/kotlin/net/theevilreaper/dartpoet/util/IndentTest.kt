package net.theevilreaper.dartpoet.util

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IndentTest {

    companion object {

        @JvmStatic
        private fun indentTest() = Stream.of(
            Arguments.of(" ", true),
            Arguments.of("  ", true),
            Arguments.of("", false),
            Arguments.of(" a", false),
            Arguments.of("123", false),
        )
    }

    @ParameterizedTest
    @MethodSource("indentTest")
    fun `test indent pattern`(indent: String, expected: Boolean) {
        if (expected) {
            assertTrue { isIndent(indent) }
        } else {
            assertFalse { isIndent(indent) }
        }
    }
}
