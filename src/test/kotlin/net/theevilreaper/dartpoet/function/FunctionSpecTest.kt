package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.NO_PARAMETER_TYPE
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.ArgumentUtils
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Test function spec creation")
class FunctionSpecTest {

    companion object {

        @JvmStatic
        private fun invalidFunctionTests(): Stream<Arguments> = Stream.of(
            Arguments.of(
                { FunctionSpec.builder(" ").build() },
                IllegalArgumentException::class.java,
                "The name of a function can't be empty"
            ),
            Arguments.of(
                { FunctionSpec.builder("getName").modifier(DartModifier.ABSTRACT).addCode("%L", "value").build() },
                IllegalArgumentException::class.java,
                "An abstract method can't have a body"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.builder("name", String::class).initializer("%C", "theEvilReaper").build(),
                            ParameterSpec.builder("value", Int::class).modifiers(DartModifier.REQUIRED).build()
                        )
                        .build()
                },
                IllegalArgumentException::class.java,
                "A function can't have required and optional parameters"
            )
        )

        @JvmStatic
        private fun testParameterGeneration(): Stream<Arguments> = Stream.of(
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.builder("text", String::class).initializer("%C", "Hello World").build()
                        )
                        .build()
                },
                "abstract void print([String text = 'Hello World']);"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.builder("text", String::class).required().build(),
                            ParameterSpec.builder("name", String::class).required().build()
                        )
                        .build()
                },
                "abstract void print({required String text, required String name});"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.builder("text", String::class).required().build(),
                            ParameterSpec.builder("count", Int::class).build(),
                            ParameterSpec.builder("name", String::class).required().build(),
                        )
                        .build()
                },
                "abstract void print(int count, {required String text, required String name});"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.builder("text", String::class).build(),
                            ParameterSpec.builder("additional", String::class).initializer("%C", "Hello World!")
                                .build(),
                        )
                        .build()
                },
                "abstract void print(String text, [String additional = 'Hello World!']);"
            ),
        )

        @JvmStatic
        private fun invalidFunctionParameters() = Stream.of(
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameter(ParameterSpec.builder("name").build())
                },
                NO_PARAMETER_TYPE
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameter { ParameterSpec.builder("name").build() }
                },
                NO_PARAMETER_TYPE
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameters(
                            ParameterSpec.builder("test", String::class).build(),
                            ParameterSpec.builder("name").build()
                        )
                },
                NO_PARAMETER_TYPE
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidFunctionTests")
    fun `test invalid function`(specBuilder: () -> Unit, expected: Class<Exception>, message: String) {
        assertThrows(expected, specBuilder, message)
    }

    @ParameterizedTest
    @MethodSource("testParameterGeneration")
    fun `test different parameter variants in combination`(specBuilder: () -> FunctionSpec, expected: String) {
        val spec = specBuilder.invoke()
        assertEquals(expected, spec.toString())
    }

    @ParameterizedTest
    @MethodSource("invalidFunctionParameters")
    fun `test invalid parameters on functions`(specBuilder: () -> Unit, expected: String) {
        assertThrows(IllegalStateException::class.java, specBuilder, expected)
    }

    @Test
    fun `test spec to builder conversation`() {
        val functionSpec = FunctionSpec.builder("getAmount")
            .returns(Int::class.asTypeName().copy(nullable = true))
            .async(false)
            .addCode("return %L", "10")
            .build()
        val specAsBuilder = functionSpec.toBuilder()
        assertEquals(functionSpec.name, specAsBuilder.name)
        assertEquals(functionSpec.returnType, specAsBuilder.returnType)
        assertFalse { specAsBuilder.async }
        assertTrue { specAsBuilder.returnType!!.isNullable }
        assertTrue { specAsBuilder.body.isNotEmpty() }
    }
}
