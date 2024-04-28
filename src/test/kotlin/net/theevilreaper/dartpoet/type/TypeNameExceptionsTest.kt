package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SPACE_STRING
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test cases which raise an exception when creating a TypeName")
class TypeNameExceptionsTest {

    companion object {

        @JvmStatic
        private fun testTypeNameConditions() = Stream.of(
            Arguments.of({ ClassName(EMPTY_STRING) }, "The name of a ClassName can't be empty (includes only spaces)"),
            Arguments.of({ ClassName(SPACE_STRING) }, "The name of a ClassName can't be empty (includes only spaces)"),
            Arguments.of(
                {
                    ParameterizedTypeName(
                        enclosingTypeName = null,
                        rawType = List::class.asClassName(),
                        typeArguments = listOf()
                    )
                },
                "no type arguments: List",
            ),
        )
    }

    @Test
    fun `test copy method from class name implementation which throws an IllegalAccessException`() {
        val exception = assertThrows<IllegalAccessException> { DYNAMIC.copy() }
        assertEquals("The dynamic type can't be copied", exception.message)
    }

    @ParameterizedTest
    @MethodSource("testTypeNameConditions")
    fun `test type name condition on object creation which triggers an exception`(
        objectCall: () -> Unit,
        exceptionMessage: String,
    ) {
        val exception = assertThrows<IllegalArgumentException> { objectCall() }
        assertEquals(exceptionMessage, exception.message)
    }
}
