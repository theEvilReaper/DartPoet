package net.theevilreaper.dartpoet.classTypes

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test the validation of exclusive class modifiers")
class ClassModifierValidationTest {

    companion object {

        @JvmStatic
        private fun invalidModifierCombinations() = Stream.of(
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.BASE }
                        .modifier { DartModifier.INTERFACE }
                        .build()
                },
                "A class can only have one of these modifiers at the same time: [BASE, INTERFACE, FINAL, SEALED], but got: [BASE, INTERFACE]"
            ),
            Arguments.of(
                {
                    ClassSpec.builder("Handler")
                        .modifier { DartModifier.SEALED }
                        .modifier { DartModifier.FINAL }
                        .build()
                },
                "A class can only have one of these modifiers at the same time: [BASE, INTERFACE, FINAL, SEALED], but got: [SEALED, FINAL]"
            ),
            Arguments.of(
                {
                    ClassSpec.abstractClass("Handler")
                        .modifier { DartModifier.SEALED }
                        .build()
                },
                "A sealed class can't be combined with the abstract modifier because sealed classes are implicitly abstract"
            )
        )
    }

    @ParameterizedTest(name = "Test cases for invalid class modifier combinations")
    @MethodSource("invalidModifierCombinations")
    fun `test invalid class modifier combination`(classSpec: () -> Unit, message: String) {
        val exception = assertThrows<IllegalStateException> { classSpec() }
        assertEquals(message, exception.message)
    }
}
