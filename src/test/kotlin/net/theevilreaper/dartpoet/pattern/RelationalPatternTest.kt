package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.code.CodeBlock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the RelationalPattern implementation")
class RelationalPatternTest {

    companion object {

        @JvmStatic
        private fun operators() = Stream.of(
            Arguments.of(RelationalOperator.EQUAL, "== 0"),
            Arguments.of(RelationalOperator.NOT_EQUAL, "!= 0"),
            Arguments.of(RelationalOperator.LESS, "< 0"),
            Arguments.of(RelationalOperator.LESS_OR_EQUAL, "<= 0"),
            Arguments.of(RelationalOperator.GREATER, "> 0"),
            Arguments.of(RelationalOperator.GREATER_OR_EQUAL, ">= 0"),
        )
    }

    @ParameterizedTest(name = "Test relational pattern rendering: {1}")
    @MethodSource("operators")
    fun `test relational operators`(operator: RelationalOperator, expected: String) {
        assertEquals(expected, Pattern.relational(operator, 0).toString())
    }

    @Test
    fun `test relational pattern with string and constant operands`() {
        assertEquals("== 'a'", Pattern.relational(RelationalOperator.EQUAL, "a").toString())
        assertEquals("!= null", Pattern.relational(RelationalOperator.NOT_EQUAL, null).toString())
        assertEquals(">= maxValue", Pattern.relational(RelationalOperator.GREATER_OR_EQUAL, CodeBlock.of("maxValue")).toString())
    }
}
