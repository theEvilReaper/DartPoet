package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test part directive creation")
class PartDirectiveTest {

    private val expectedImport = "part 'item_model.freezed.dart';"

    companion object {
        @JvmStatic
        private fun invalidPartDirectives(): Stream<Arguments> = Stream.of(
            Arguments.of(
                { DirectiveFactory.create(DirectiveType.IMPORT, " ") },
                "The path of an Import can't be empty"
            ),
            Arguments.of(
                { DirectiveFactory.create(DirectiveType.RELATIVE, " ") },
                "The path of an Import can't be empty"
            )
        )
    }

    @ParameterizedTest(name = "Test invalid directive creation over the DirectiveFactory")
    @MethodSource("invalidPartDirectives")
    fun `test import with empty path`(current: () -> Directive, expectedMessage: String) {
        assertThrows<IllegalStateException>(expectedMessage) { current() }
    }

    @Test
    fun `create part import`() {
        val partImport = DirectiveFactory.create(DirectiveType.PART, "item_model.freezed.dart")
        assertEquals(expectedImport, partImport.asString())
    }
}