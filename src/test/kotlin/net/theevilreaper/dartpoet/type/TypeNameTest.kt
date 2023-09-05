package net.theevilreaper.dartpoet.type

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
}
