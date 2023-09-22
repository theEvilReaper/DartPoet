package net.theevilreaper.dartpoet.type

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TypeNameExceptionsTest {

    companion object {

        @JvmStatic
        private fun copyWithException() = Stream.of(
            Arguments.of("The dynamic type can't be copied", { DYNAMIC.copy() }),
            Arguments.of("The const type can't be copied", { CONST.copy() })
        )

        @JvmStatic
        private fun testTypeNameConditions() = Stream.of(
            Arguments.of("The name of a ClassName can't be empty (includes only spaces)", { ClassName("") }),
            Arguments.of("The name of a ClassName can't be empty (includes only spaces)", { ClassName("  ") }),
            Arguments.of("no type arguments: List", {
                ParameterizedTypeName(
                    enclosingTypeName = null,
                    rawType = List::class.asClassName(),
                    typeArguments = listOf()
                )
            }),
        )
    }

    @ParameterizedTest
    @MethodSource("copyWithException")
    fun `test copy method from class name implementation which throws an IllegalAccessException`(
        exceptionMessage: String,
        methodCall: () -> Unit
    ) {
        val exception = assertThrows<IllegalAccessException> { methodCall() }
        assertEquals(exceptionMessage, exception.message)
    }

    @ParameterizedTest
    @MethodSource("testTypeNameConditions")
    fun `test type name condition on object creation which triggers an exception`(
        exceptionMessage: String,
        objectCall: () -> Unit
    ) {
        val exception = assertThrows<IllegalArgumentException> { objectCall() }
        assertEquals(exceptionMessage, exception.message)
    }
}
