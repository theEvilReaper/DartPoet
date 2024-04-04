package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Test if a given indent is a valid indent for the project")
class IndentTest {

    companion object {
        @JvmStatic
        private fun testValidIndents() = Stream.of(
            Arguments.of(" "),
            Arguments.of("  "),
        )

        @JvmStatic
        private fun testInvalidIndents() = Stream.of(
            Arguments.of(""),
            Arguments.of(" a"),
            Arguments.of("123"),
        )
    }

    @ParameterizedTest(name = "Test valid indent value: {arguments}")
    @MethodSource("testValidIndents")
    fun `test valid indent pattern`(indent: String) {
        assertTrue { isIndent(indent) }
    }

    @ParameterizedTest(name = "Test invalid indent value: {arguments}")
    @MethodSource("testInvalidIndents")
    fun `test invalid indent patterns`(indent: String) {
        assertFalse { isIndent(indent) }
    }
}
