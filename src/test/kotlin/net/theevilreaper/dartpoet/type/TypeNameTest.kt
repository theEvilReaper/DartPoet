package net.theevilreaper.dartpoet.type

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.IllegalArgumentException
import java.util.function.BiFunction
import java.util.stream.Stream

@DisplayName("Test some cases for the conversation from a class to a TypeName")
class TypeNameTest {

    companion object {

        @JvmStatic
        private fun primitiveTests() = Stream.of(
            Arguments.of("int", Int::class.asTypeName()),
            Arguments.of("int", Long::class.asClassName()),
            Arguments.of("String", String::class.asTypeName()),
            Arguments.of("double", Double::class.asTypeName()),
            Arguments.of("double", Float::class.asClassName()),
            Arguments.of("bool", Boolean::class.asTypeName())
        )

        @JvmStatic
        private fun invalidTests() = Stream.of(
            Arguments.of("Array", "An array type is not supported at the moment", { TypeName.get(intArrayOf()::class.java) }),
            Arguments.of("Ulong", "The given ${ULong::class} is not a primitive object", { TypeName.parseSimpleKClass(ULong::class) })
        )
    }

    @ParameterizedTest(name = "Test primitive cases: {0}")
    @MethodSource("primitiveTests")
    fun `test primitive type conversation to a ClassName`(expectedType: String, type: TypeName) {
        assertEquals(expectedType, type.toString())
    }

    @ParameterizedTest(name = "Test invalid cases: {0}")
    @MethodSource("invalidTests")
    fun `test invalid cases`(expectedType: String, expectedMessage: String, objectCall: () -> Unit) {
        val exception = assertThrows<IllegalArgumentException> { objectCall() }
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `test get typeName with the java type which is not an array`() {
        val className = BiFunction::class.asClassName()
        val typeNameFromMethod = TypeName.get(BiFunction::class.java)
        assertEquals(className, typeNameFromMethod)
    }

    @Test
    fun `test parseSimpleKClass with kotlin type class`() {
        val className = String::class.asClassName()
        val typeNameFromMethod = TypeName.parseSimpleKClass(String::class)
        assertEquals(className, typeNameFromMethod)
    }
}
