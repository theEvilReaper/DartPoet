package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.body.FunctionBodyType
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.NO_PARAMETER_TYPE
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
                { FunctionSpec.builder("getName").modifier(DartModifier.EXTERNAL).addCode("%L", "value").build() },
                IllegalArgumentException::class.java,
                "An external function can't have a body"
            ),
            Arguments.of(
                { FunctionSpec.builder("getName").modifiers(DartModifier.ABSTRACT, DartModifier.EXTERNAL).build() },
                IllegalArgumentException::class.java,
                "A function can't be both abstract and external"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("print")
                        .modifiers(DartModifier.ABSTRACT)
                        .parameters(
                            ParameterSpec.optional("name", String::class).initializer("%C", "theEvilReaper").build(),
                            ParameterSpec.required("value", Int::class).build()
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
                            ParameterSpec.optional("text", String::class).initializer("%C", "Hello World").build()
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
                            ParameterSpec.required("text", String::class).build(),
                            ParameterSpec.required("name", String::class).build()
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
                            ParameterSpec.required("text", String::class).build(),
                            ParameterSpec.positional("count", Int::class).build(),
                            ParameterSpec.required("name", String::class).build(),
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
                            ParameterSpec.positional("text", String::class).build(),
                            ParameterSpec.optional("additional", String::class).initializer("%C", "Hello World!")
                                .build(),
                        )
                        .build()
                },
                "abstract void print(String text, [String additional = 'Hello World!']);"
            ),
        )

        @JvmStatic
        private fun invalidGeneratorFunctions(): Stream<Arguments> = Stream.of(
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .returns(ClassName("Iterable").parameterizedBy(INTEGER))
                        .body(FunctionBodyType.SYNC_STAR)
                        .build()
                },
                "A function with the sync* modifier must have a body"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("load")
                        .modifier(DartModifier.ABSTRACT)
                        .returns(ClassName("Future").parameterizedBy(ClassName("void")))
                        .body(FunctionBodyType.ASYNC)
                        .build()
                },
                "A function with the async modifier must have a body"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .returns(ClassName("Stream").parameterizedBy(INTEGER))
                        .body(FunctionBodyType.ASYNC_STAR)
                        .type(FunctionType.SHORTEN)
                        .addCode("%L", "Stream.empty();")
                        .build()
                },
                "A generator function (async*) can't use the arrow syntax"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .accessorType(MethodAccessorType.SETTER)
                        .parameter(ParameterSpec.positional("value", INTEGER).build())
                        .body(FunctionBodyType.SYNC_STAR)
                        .addCode("%L", "yield value;")
                        .build()
                },
                "A setter can't use the sync* modifier"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .accessorType(MethodAccessorType.SETTER)
                        .parameter(ParameterSpec.positional("value", INTEGER).build())
                        .body(FunctionBodyType.ASYNC)
                        .addCode("%L", "print(value);")
                        .build()
                },
                "A setter can't use the async modifier"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .returns(ClassName("Iterable").parameterizedBy(INTEGER))
                        .body(FunctionBodyType.ASYNC_STAR)
                        .addCode("%L", "yield 1;")
                        .build()
                },
                "A async* function must return Stream<T>, but was Iterable<int>"
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("values")
                        .returns(INTEGER)
                        .body(FunctionBodyType.SYNC_STAR)
                        .addCode("%L", "yield 1;")
                        .build()
                },
                "A sync* function must return Iterable<T>, but was int"
            ),
        )

        @JvmStatic
        private fun invalidFunctionParameters() = Stream.of(
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameter(ParameterSpec.positional("name").build())
                },
                NO_PARAMETER_TYPE
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameter { ParameterSpec.positional("name").build() }
                },
                NO_PARAMETER_TYPE
            ),
            Arguments.of(
                {
                    FunctionSpec.builder("Test")
                        .parameters(
                            ParameterSpec.positional("test", String::class).build(),
                            ParameterSpec.positional("name").build()
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

    @ParameterizedTest
    @MethodSource("invalidGeneratorFunctions")
    fun `test invalid generator functions`(specBuilder: () -> Unit, message: String) {
        val exception = assertThrows(IllegalArgumentException::class.java, specBuilder)
        assertEquals(message, exception.message)
    }

    @Test
    fun `test generator function allows dynamic return type`() {
        val functionSpec = FunctionSpec.builder("values")
            .returns(DYNAMIC)
            .body(FunctionBodyType.SYNC_STAR)
            .addCode("%L", "yield 1;")
            .build()
        assertEquals(FunctionBodyType.SYNC_STAR, functionSpec.bodyType)
    }

    @Test
    fun `test body type overrides deprecated async flag`() {
        @Suppress("DEPRECATION")
        val builder = FunctionSpec.builder("values")
            .async(true)
            .body(FunctionBodyType.ASYNC_STAR)
        assertFalse(builder.async)
        assertEquals(FunctionBodyType.ASYNC_STAR, builder.bodyType)
    }

    @Test
    fun `test body type is kept on builder conversation`() {
        val functionSpec = FunctionSpec.builder("values")
            .returns(ClassName("Stream").parameterizedBy(INTEGER))
            .body(FunctionBodyType.ASYNC_STAR)
            .addCode("%L", "yield 1;")
            .build()
        assertEquals(FunctionBodyType.ASYNC_STAR, functionSpec.toBuilder().bodyType)
    }

    @Test
    fun `test spec to builder conversation`() {
        val functionSpec = FunctionSpec.builder("getAmount")
            .returns(Int::class.asTypeName().copy(nullable = true))
            .async(false)
            .generic("T", ClassName("Comparable"))
            .addCode("return %L", "10")
            .build()
        val specAsBuilder = functionSpec.toBuilder()
        assertEquals(functionSpec.name, specAsBuilder.name)
        assertEquals(functionSpec.returnType, specAsBuilder.returnType)
        assertFalse { specAsBuilder.async }
        assertTrue { specAsBuilder.body.isNotEmpty() }
        assertEquals(functionSpec.genericCasts, specAsBuilder.genericCasts)
    }

    @Test
    fun `test generic function with string overload`() {
        val functionSpec = FunctionSpec.builder("identity")
            .generic("T")
            .returns(ClassName("T"))
            .parameter(ParameterSpec.positional("value", ClassName("T")).build())
            .addCode("return value;")
            .build()
        assertEquals(
            """
            |T identity<T>(T value) {
            |  return value;
            |}
            """.trimMargin(),
            functionSpec.toString()
        )
    }
}
