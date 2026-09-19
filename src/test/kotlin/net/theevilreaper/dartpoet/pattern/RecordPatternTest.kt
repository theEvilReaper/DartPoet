package net.theevilreaper.dartpoet.pattern

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the RecordPattern implementation")
class RecordPatternTest {

    companion object {

        @JvmStatic
        private fun recordPatterns() = Stream.of(
            Arguments.of(
                "()",
                Pattern.record()
            ),
            Arguments.of(
                "(0, 0)",
                Pattern.record(Pattern.literal(0), Pattern.literal(0))
            ),
            Arguments.of(
                "(var x, var y)",
                Pattern.record(Pattern.variable("x"), Pattern.variable("y"))
            ),
            Arguments.of(
                "(name: var name, age: var age)",
                Pattern.record(
                    positional = emptyList(),
                    named = linkedMapOf("name" to Pattern.variable("name"), "age" to Pattern.variable("age"))
                )
            ),
            Arguments.of(
                "(0, name: var name)",
                Pattern.record(
                    positional = listOf(Pattern.literal(0)),
                    named = linkedMapOf("name" to Pattern.variable("name"))
                )
            ),
            Arguments.of(
                "(var x, (var y, var z))",
                Pattern.record(Pattern.variable("x"), Pattern.record(Pattern.variable("y"), Pattern.variable("z")))
            ),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("recordPatterns")
    fun `test record pattern write`(expected: String, pattern: Pattern) {
        assertEquals(expected, pattern.toString())
    }
}
