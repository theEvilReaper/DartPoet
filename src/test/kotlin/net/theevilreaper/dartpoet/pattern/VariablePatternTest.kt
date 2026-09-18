package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.INTEGER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the VariablePattern implementation")
class VariablePatternTest {

    companion object {

        @JvmStatic
        private fun variablePatterns() = Stream.of(
            Arguments.of("var x", Pattern.variable("x")),
            Arguments.of("final x", Pattern.variable("x", isFinal = true)),
            Arguments.of("int x", Pattern.variable("x", INTEGER)),
            Arguments.of("final int x", Pattern.variable("x", INTEGER, isFinal = true)),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("variablePatterns")
    fun `test variable pattern write`(expected: String, pattern: Pattern) {
        assertEquals(expected, pattern.toString())
    }

    @Test
    fun `test variable pattern throws for blank name`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Pattern.variable("   ")
        }
        assertEquals("The name of a variable pattern can't be empty", exception.message)
    }
}
