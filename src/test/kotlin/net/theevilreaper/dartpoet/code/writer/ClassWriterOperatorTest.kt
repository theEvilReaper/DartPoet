package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.operator.UnaryOperator
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.BOOLEAN
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.VOID
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test operator generation as part of a class")
class ClassWriterOperatorTest {

    private companion object {

        @JvmStatic
        private fun classesWithOperators() = Stream.of(
            Arguments.of(
                ClassSpec.builder("Vector")
                    .property {
                        PropertySpec.builder("x", INTEGER).build()
                    }
                    .property {
                        PropertySpec.builder("y", INTEGER).build()
                    }
                    .constructor(
                        ConstructorSpec.builder("Vector")
                            .parameter(ParameterSpec.positional("x").build())
                            .parameter(ParameterSpec.positional("y").build())
                            .build()
                    )
                    .operator(
                        DartOperatorSpec.builder(BinaryOperator.PLUS)
                            .returnType(ClassName("Vector"))
                            .parameter(ParameterSpec.positional("other", ClassName("Vector")).build())
                            .addCode("return %L;", "Vector(x + other.x, y + other.y)")
                            .build()
                    )
                    .build(),
                """
                |class Vector {
                |
                |  int x;
                |  int y;
                |
                |  Vector(this.x, this.y);
                |
                |  Vector operator +(Vector other) {
                |    return Vector(x + other.x, y + other.y);
                |  }
                |}
                """.trimMargin()
            ),
            Arguments.of(
                ClassSpec.builder("Box")
                    .property {
                        PropertySpec.builder("_open", BOOLEAN).initWith("false").build()
                    }
                    .function(
                        FunctionSpec.builder("open")
                            .returns(VOID)
                            .addCode("_open = !_open;")
                            .build()
                    )
                    .operator(
                        DartOperatorSpec.builder(UnaryOperator.NEGATE)
                            .returnType(ClassName("Box"))
                            .addCode("return %L;", "Box()")
                            .build()
                    )
                    .build(),
                """
                |class Box {
                |
                |  bool _open = false;
                |
                |  void open() {
                |    _open = !_open;
                |  }
                |
                |  Box operator -() {
                |    return Box();
                |  }
                |}
                """.trimMargin()
            ),
            Arguments.of(
                ClassSpec.builder("Money")
                    .property {
                        PropertySpec.builder("cents", INTEGER).build()
                    }
                    .constructor(
                        ConstructorSpec.builder("Money")
                            .parameter(ParameterSpec.positional("cents").build())
                            .build()
                    )
                    .function(
                        FunctionSpec.builder("isZero")
                            .returns(BOOLEAN)
                            .addCode("return cents == 0;")
                            .build()
                    )
                    .operator(
                        DartOperatorSpec.builder(BinaryOperator.MINUS)
                            .returnType(ClassName("Money"))
                            .parameter(ParameterSpec.positional("other", ClassName("Money")).build())
                            .addCode("return %L;", "Money(cents - other.cents)")
                            .build()
                    )
                    .operator(
                        DartOperatorSpec.builder(UnaryOperator.NEGATE)
                            .returnType(ClassName("Money"))
                            .addCode("return %L;", "Money(-cents)")
                            .build()
                    )
                    .build(),
                """
                |class Money {
                |
                |  int cents;
                |
                |  Money(this.cents);
                |
                |  bool isZero() {
                |    return cents == 0;
                |  }
                |
                |  Money operator -(Money other) {
                |    return Money(cents - other.cents);
                |  }
                |
                |  Money operator -() {
                |    return Money(-cents);
                |  }
                |}
                """.trimMargin()
            )
        )
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("classesWithOperators")
    fun `test classes with operators`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test duplicate operator symbol throws`() {
        assertThrows(IllegalStateException::class.java) {
            ClassSpec.builder("Vector")
                .operator(
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(ClassName("Vector"))
                        .parameter(ParameterSpec.positional("other", ClassName("Vector")).build())
                        .addCode("return %L;", "this")
                        .build()
                )
                .operator(
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(ClassName("Vector"))
                        .parameter(ParameterSpec.positional("other", ClassName("Vector")).build())
                        .addCode("return %L;", "this")
                        .build()
                )
                .build()
        }
    }

    @Test
    fun `test operator on anonymous class throws`() {
        assertThrows(IllegalStateException::class.java) {
            ClassSpec.anonymousClassBuilder()
                .operator(
                    DartOperatorSpec.builder(UnaryOperator.NEGATE)
                        .returnType(BOOLEAN)
                        .addCode("return %L;", "true")
                        .build()
                )
                .build()
        }
    }
}
