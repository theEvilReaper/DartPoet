package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.type.ClassName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the ObjectPattern implementation")
class ObjectPatternTest {

    companion object {

        @JvmStatic
        private fun objectPatterns() = Stream.of(
            Arguments.of(
                "Point()",
                Pattern.objectPattern(ClassName("Point"), emptyMap())
            ),
            Arguments.of(
                "Point(x: var px, y: var py)",
                Pattern.objectPattern(
                    ClassName("Point"),
                    linkedMapOf("x" to Pattern.variable("px"), "y" to Pattern.variable("py"))
                )
            ),
            Arguments.of(
                "Rect(width: var w, height: var h)",
                Pattern.objectPattern(
                    ClassName("Rect"),
                    linkedMapOf("width" to Pattern.variable("w"), "height" to Pattern.variable("h"))
                )
            ),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("objectPatterns")
    fun `test object pattern write`(expected: String, pattern: Pattern) {
        assertEquals(expected, pattern.toString())
    }
}
