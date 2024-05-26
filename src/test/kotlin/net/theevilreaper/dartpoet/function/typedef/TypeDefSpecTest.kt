package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test TypeDefSpec creation")
class TypeDefSpecTest {

    companion object {

        @JvmStatic
        private fun invalidTypeDefs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "Empty name",
                { TypeDefSpec.builder(EMPTY_STRING).build() },
                "The name of a typedef can't be empty"
            ),
            Arguments.of(
                "Empty function name",
                {
                    TypeDefSpec.builder("Test", Int::class)
                        .name(EMPTY_STRING)
                        .returns(String::class).build()
                },
                "The function name of a typedef can't be empty"
            )
        )
    }

    @ParameterizedTest(name = "Test invalid typedef definitions with: {0}")
    @MethodSource("invalidTypeDefs")
    fun `test invalid typedef creation`(name: String, function: () -> Unit, message: String) {
        val exception = assertThrows<IllegalArgumentException> { function() }
        assertEquals(message, exception.message)
    }

    @Test
    fun `test to builder method`() {
        val typeSpec = TypeDefSpec.builder("Test", Int::class)
            .name("Function")
            .returns(String::class).build()
        assertNotEquals(Void::class.java, typeSpec.returnType)

        val newBuilder = typeSpec.toBuilder()
        assertEquals(typeSpec.returnType, newBuilder.returnType)
        assertEquals(typeSpec.name, newBuilder.name)
        newBuilder.parameter(ParameterSpec.builder("test", String::class).build())

        val newTypeSpec = newBuilder.build()
        assertNotEquals(typeSpec.parameters, newTypeSpec.parameters)
    }
}