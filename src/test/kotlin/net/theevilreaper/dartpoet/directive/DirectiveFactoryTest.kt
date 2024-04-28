package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SPACE_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test usage of the DirectiveFactory utility class")
class DirectiveFactoryTest {

    companion object {

        @JvmStatic
        private fun invalidFactoryUsage() = Stream.of(
            Arguments.of(
                { DirectiveFactory.create(DirectiveType.LIBRARY, EMPTY_STRING) },
                "The library directive doesn't support a cast type or import cast. Please use #createLibDirective method instead"
            ),
            Arguments.of(
                { DirectiveFactory.create(DirectiveType.LIBRARY, EMPTY_STRING, castType = CastType.HIDE) },
                "The library directive doesn't support a cast type or import cast. Please use #createLibDirective method instead"
            ),
        )
    }

    @ParameterizedTest
    @EnumSource(value = DirectiveType::class, mode = EnumSource.Mode.MATCH_ALL)
    fun `test invalid import mapping`(directiveType: DirectiveType) {
        assertThrows<IllegalStateException> { DirectiveFactory.create(directiveType, SPACE_STRING) }
    }

    @ParameterizedTest
    @MethodSource("invalidFactoryUsage")
    fun `test invalid factory usage`(current: () -> Directive, expectedMessage: String) {
        val exception = assertThrows<IllegalStateException> { current() }
        assertEquals(IllegalStateException::class.java, exception.javaClass)
        assertEquals(expectedMessage, exception.message)
    }
}
