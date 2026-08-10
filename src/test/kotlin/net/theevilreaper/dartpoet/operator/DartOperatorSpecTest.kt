package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.VOID
import net.theevilreaper.dartpoet.util.NO_PARAMETER_TYPE
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Test DartOperatorSpec creation")
class DartOperatorSpecTest {

    private companion object {

        @JvmStatic
        private fun invalidOperatorArity(): Stream<Arguments> = Stream.of(
            Arguments.of(
                {
                    DartOperatorSpec.builder(UnaryOperator.NEGATE)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.positional("other", INTEGER).build())
                        .addCode("return %L;", "-value")
                        .build()
                },
                "Operator '-' requires exactly 0 parameter(s), but got 1"
            ),
            Arguments.of(
                {
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .addCode("return %L;", "value")
                        .build()
                },
                "Operator '+' requires exactly 1 parameter(s), but got 0"
            ),
            Arguments.of(
                {
                    DartOperatorSpec.builder(IndexOperator.INDEX)
                        .returnType(INTEGER)
                        .addCode("return %L;", "value")
                        .build()
                },
                "Operator '[]' requires exactly 1 parameter(s), but got 0"
            ),
            Arguments.of(
                {
                    DartOperatorSpec.builder(IndexOperator.INDEX_ASSIGN)
                        .returnType(VOID)
                        .parameter(ParameterSpec.positional("index", INTEGER).build())
                        .addCode("%L = %L;", "_values[index]", "value")
                        .build()
                },
                "Operator '[]=' requires exactly 2 parameter(s), but got 1"
            )
        )

        @JvmStatic
        private fun invalidOperatorParameterKinds(): Stream<Arguments> = Stream.of(
            Arguments.of(
                {
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.named("other", INTEGER).build())
                        .addCode("return %L;", "value")
                        .build()
                },
                "Operator parameters must be simple positional parameters, but got NAMED for 'other'"
            ),
            Arguments.of(
                {
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.optional("other", INTEGER).build())
                        .addCode("return %L;", "value")
                        .build()
                },
                "Operator parameters must be simple positional parameters, but got OPTIONAL for 'other'"
            ),
            Arguments.of(
                {
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.required("other", INTEGER).build())
                        .addCode("return %L;", "value")
                        .build()
                },
                "Operator parameters must be simple positional parameters, but got REQUIRED for 'other'"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidOperatorArity")
    fun `test invalid operator arity`(specBuilder: () -> DartOperatorSpec, message: String) {
        val exception = assertThrows(IllegalStateException::class.java) { specBuilder() }
        assertEquals(message, exception.message)
    }

    @ParameterizedTest
    @MethodSource("invalidOperatorParameterKinds")
    fun `test invalid operator parameter kinds`(specBuilder: () -> DartOperatorSpec, message: String) {
        val exception = assertThrows(IllegalStateException::class.java) { specBuilder() }
        assertEquals(message, exception.message)
    }

    @Test
    fun `test operator parameter with default value throws`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            DartOperatorSpec.builder(BinaryOperator.PLUS)
                .returnType(INTEGER)
                .parameter(ParameterSpec.positional("other", INTEGER).initializer("%L", "0").build())
                .addCode("return %L;", "value")
                .build()
        }
        assertEquals("Operator parameters can't have a default value ('other')", exception.message)
    }

    @Test
    fun `test operator parameter without a type throws`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            DartOperatorSpec.builder(BinaryOperator.PLUS)
                .returnType(INTEGER)
                .parameter(ParameterSpec.positional("other").build())
                .addCode("return %L;", "value")
                .build()
        }
        assertEquals(NO_PARAMETER_TYPE, exception.message)
    }

    @Test
    fun `test operator parameters without a type throws`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            DartOperatorSpec.builder(IndexOperator.INDEX_ASSIGN)
                .returnType(VOID)
                .parameters(
                    listOf(
                        ParameterSpec.positional("index", INTEGER).build(),
                        ParameterSpec.positional("value").build()
                    )
                )
                .addCode("%L = %L;", "_values[index]", "value")
                .build()
        }
        assertEquals(NO_PARAMETER_TYPE, exception.message)
    }

    @Test
    fun `test operator without a return type throws`() {
        assertThrows(IllegalStateException::class.java) {
            DartOperatorSpec.builder(UnaryOperator.NEGATE)
                .addCode("return %L;", "-value")
                .build()
        }
    }

    @Test
    fun `test operator without a body throws`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            DartOperatorSpec.builder(UnaryOperator.NEGATE)
                .returnType(INTEGER)
                .build()
        }
        assertEquals("An operator must have a body", exception.message)
    }

    @Test
    fun `test spec to builder conversion`() {
        val operatorSpec = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(INTEGER)
            .parameter(ParameterSpec.positional("other", INTEGER).build())
            .doc("Adds two values.")
            .annotation(AnnotationSpec.builder("override").build())
            .addCode("return %L;", "value + other")
            .build()

        val specAsBuilder = operatorSpec.toBuilder()

        assertEquals(operatorSpec.operator, specAsBuilder.operator)
        assertEquals(operatorSpec.returnType, specAsBuilder.returnType)
        assertEquals(operatorSpec.parameters, specAsBuilder.parameters)
        assertEquals(operatorSpec.docs, specAsBuilder.docs)
        assertEquals(operatorSpec.annotations, specAsBuilder.annotationData.annotations.toSet())
        assertEquals(operatorSpec.type, specAsBuilder.type)
        assertTrue { specAsBuilder.body.isNotEmpty() }
    }
}
