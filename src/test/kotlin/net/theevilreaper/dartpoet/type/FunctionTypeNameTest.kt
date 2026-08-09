package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the FunctionTypeName implementation")
class FunctionTypeNameTest {

    companion object {

        @JvmStatic
        private fun functionTypeNames() = Stream.of(
            Arguments.of(
                "void Function()",
                FunctionTypeName.builder().build()
            ),
            Arguments.of(
                "int Function(String value)",
                FunctionTypeName.builder()
                    .returns(INTEGER)
                    .parameter(ParameterSpec.positional("value", STRING).build())
                    .build()
            ),
            Arguments.of(
                "void Function(int a, int b)",
                FunctionTypeName.builder()
                    .parameters(
                        ParameterSpec.positional("a", INTEGER).build(),
                        ParameterSpec.positional("b", INTEGER).build()
                    )
                    .build()
            ),
            Arguments.of(
                "String Function()",
                FunctionTypeName.builder().returns(String::class).build()
            ),
            Arguments.of(
                "MyType Function()",
                FunctionTypeName.builder().returns(ClassName("MyType")).build()
            ),
            Arguments.of(
                "T Function(T value)",
                FunctionTypeName.builder()
                    .returns(ClassName("T"))
                    .parameter(ParameterSpec.positional("value", ClassName("T")).build())
                    .build()
            ),
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("functionTypeNames")
    fun `test function type name write`(expected: String, functionType: FunctionTypeName) {
        assertEquals(expected, functionType.toString())
    }

    @Test
    fun `test nullable function type through property writer`() {
        val property = PropertySpec.builder(
            "onDone",
            FunctionTypeName.builder().nullable().build()
        ).build()
        assertEquals("void Function()? onDone;", property.toString())
    }

    @Test
    fun `test nullable function type with parameters through property writer`() {
        val property = PropertySpec.builder(
            "onComplete",
            FunctionTypeName.builder()
                .returns(INTEGER)
                .parameter(ParameterSpec.positional("value", STRING).build())
                .nullable()
                .build()
        ).build()
        assertEquals("int Function(String value)? onComplete;", property.toString())
    }

    @Test
    fun `test equals and hashCode for function type name`() {
        val first = FunctionTypeName.builder()
            .returns(INTEGER)
            .parameter(ParameterSpec.positional("value", STRING).build())
            .build()
        val second = FunctionTypeName.builder()
            .returns(INTEGER)
            .parameter(ParameterSpec.positional("value", STRING).build())
            .build()
        val third = FunctionTypeName.builder()
            .returns(STRING)
            .parameter(ParameterSpec.positional("value", STRING).build())
            .build()

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertNotEquals(first, third)
    }

    @Test
    fun `test copy method from the function type name`() {
        val functionType = FunctionTypeName.builder().returns(INTEGER).build()
        assertFalse { functionType.isNullable }
        val nullableType = functionType.copy(nullable = true)
        assertTrue { nullableType.isNullable }
        // Verify through property writer (the real code generation path)
        val property = PropertySpec.builder("fn", nullableType).build()
        assertEquals("int Function()? fn;", property.toString())
    }

    @Test
    fun `test builder throws for invalid optional parameter`() {
        val exception = assertThrows<IllegalArgumentException> {
            FunctionTypeName.builder()
                .parameter(ParameterSpec.optional("value", INTEGER).build())
                .build()
        }
        assertEquals("Optional parameters must be nullable or have an initializer", exception.message)
    }
}
