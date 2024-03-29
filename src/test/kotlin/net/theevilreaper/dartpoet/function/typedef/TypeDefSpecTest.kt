package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TypeDefSpecTest {

    companion object {

        @JvmStatic
        private fun invalidTypeDefs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                IllegalArgumentException::class.java,
                { TypeDefSpec.builder("").build() },
                "The name of a typedef can't be empty"
            ),
            Arguments.of(
                IllegalArgumentException::class.java,
                { TypeDefSpec.builder("Test", Int::class)
                    .name("")
                    .returns(String::class).build()
                },
                "The function name of a typedef can't be empty"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidTypeDefs")
    fun `test invalid typedef creation`(exception: Class<Exception>, function: () -> Unit, message: String) {
        val exceptionMessage = assertThrows(exception, function, message).message
        assertEquals(message, exceptionMessage)
    }

    @Test
    fun `test to builder method`() {
        val typeSpec = TypeDefSpec.builder("Test", Int::class)
            .name("Function")
            .returns(String::class).build()
        assertNotEquals(Void::class.java, typeSpec.returnType)

        val newBuilder = typeSpec.toBuilder()
        newBuilder.parameter(ParameterSpec.builder("test", String::class).build())

        val newTypeSpec = newBuilder.build()
        assertNotEquals(typeSpec.parameters, newTypeSpec.parameters)
    }
}