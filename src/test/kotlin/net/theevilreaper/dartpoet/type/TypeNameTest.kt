package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.IllegalArgumentException
import java.lang.reflect.Type
import java.util.function.BiFunction
import java.util.stream.Stream

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
    }

    @ParameterizedTest
    @MethodSource("primitiveTests")
    fun `test primitive type conversation to a ClassName`(expectedType: String, type: TypeName) {
        assertEquals(expectedType, type.toString())
    }

    @Test
    fun `test get typeName with the java type and its an array`() {
        val arrayType = intArrayOf()
        val exception = assertThrows<IllegalArgumentException> { TypeName.get(arrayType::class.java) }
        assertEquals("An array type is not supported at the moment", exception.message)
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

    @Test
    fun `test parseSimpleKClass with raise an exception`() {
        val type = ULong::class
        val exception = assertThrows<IllegalArgumentException> { TypeName.parseSimpleKClass(type) }
        assertEquals("The given $type is not a primitive object", exception.message)
    }
}
