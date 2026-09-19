package net.theevilreaper.dartpoet.pattern

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the ListPattern implementation")
class ListPatternTest {

    companion object {

        @JvmStatic
        private fun listPatterns() = Stream.of(
            Arguments.of(
                "[]",
                Pattern.list()
            ),
            Arguments.of(
                "[var a, var b]",
                Pattern.list(Pattern.variable("a"), Pattern.variable("b"))
            ),
            Arguments.of(
                "['a', 'b', ...]",
                Pattern.listWithRest(
                    before = listOf(Pattern.literal("a"), Pattern.literal("b")),
                    rest = null
                )
            ),
            Arguments.of(
                "['a', 'b', ...rest]",
                Pattern.listWithRest(
                    before = listOf(Pattern.literal("a"), Pattern.literal("b")),
                    rest = "rest"
                )
            ),
            Arguments.of(
                "['a', ...rest, 'b']",
                Pattern.listWithRest(
                    before = listOf(Pattern.literal("a")),
                    rest = "rest",
                    after = listOf(Pattern.literal("b"))
                )
            ),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("listPatterns")
    fun `test list pattern write`(expected: String, pattern: Pattern) {
        assertEquals(expected, pattern.toString())
    }
}
