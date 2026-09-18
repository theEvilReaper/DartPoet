package net.theevilreaper.dartpoet.pattern

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the LiteralPattern implementation")
class LiteralPatternTest {

    companion object {

        @JvmStatic
        private fun literals() = Stream.of(
            Arguments.of("0", 0),
            Arguments.of("42", 42),
            Arguments.of("3.14", 3.14),
            Arguments.of("true", true),
            Arguments.of("false", false),
            Arguments.of("null", null),
            Arguments.of("'red'", "red"),
            Arguments.of("'it\\'s'", "it's"),
        )
    }

    @ParameterizedTest(name = "Test literal pattern rendering: {0}")
    @MethodSource("literals")
    fun `test literal pattern write`(expected: String, value: Any?) {
        assertEquals(expected, Pattern.literal(value).toString())
    }

    @Test
    fun `test literal pattern throws for unsupported type`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Pattern.literal(listOf(1, 2)).toString()
        }
        assertEquals("Unsupported literal pattern value: [1, 2]", exception.message)
    }
}
