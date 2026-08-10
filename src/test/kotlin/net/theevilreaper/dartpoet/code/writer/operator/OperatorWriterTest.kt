package net.theevilreaper.dartpoet.code.writer.operator

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.operator.IndexOperator
import net.theevilreaper.dartpoet.operator.UnaryOperator
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.BOOLEAN
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.VOID
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test operator writer")
class OperatorWriterTest {

    @Test
    fun `write binary operator with block body`() {
        val operator = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("Vector"))
            .parameter(ParameterSpec.positional("other", ClassName("Vector")).build())
            .addCode("return %L;", "Vector(x + other.x, y + other.y)")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |Vector operator +(Vector other) {
            |  return Vector(x + other.x, y + other.y);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write binary equals operator with shorthand body and override annotation`() {
        val operator = DartOperatorSpec.builder(BinaryOperator.EQUAL)
            .returnType(BOOLEAN)
            .parameter(ParameterSpec.positional("other", ClassName("Object")).build())
            .type(FunctionType.SHORTEN)
            .annotation(AnnotationSpec.builder("override").build())
            .addCode("other is Vector && x == other.x && y == other.y;")
            .build()

        assertThat(operator.toString()).isEqualTo(
            "@override\nbool operator ==(Object other) => other is Vector && x == other.x && y == other.y;"
        )
    }

    @Test
    fun `write unary negate operator`() {
        val operator = DartOperatorSpec.builder(UnaryOperator.NEGATE)
            .returnType(ClassName("Vector"))
            .addCode("return %L;", "Vector(-x, -y)")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |Vector operator -() {
            |  return Vector(-x, -y);
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write unary complement operator`() {
        val operator = DartOperatorSpec.builder(UnaryOperator.COMPLEMENT)
            .returnType(INTEGER)
            .addCode("return %L;", "~value")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |int operator ~() {
            |  return ~value;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write index operator`() {
        val operator = DartOperatorSpec.builder(IndexOperator.INDEX)
            .returnType(INTEGER)
            .parameter(ParameterSpec.positional("index", INTEGER).build())
            .addCode("return %L;", "_values[index]")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |int operator [](int index) {
            |  return _values[index];
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write index assign operator`() {
        val operator = DartOperatorSpec.builder(IndexOperator.INDEX_ASSIGN)
            .returnType(VOID)
            .parameter(ParameterSpec.positional("index", INTEGER).build())
            .parameter(ParameterSpec.positional("value", INTEGER).build())
            .addCode("%L = %L;", "_values[index]", "value")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |void operator []=(int index, int value) {
            |  _values[index] = value;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write operator without a body`() {
        val operator = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(INTEGER)
            .parameter(ParameterSpec.positional("other", INTEGER).build())
            .build()

        assertThat(operator.toString()).isEqualTo("int operator +(int other);")
    }

    @Test
    fun `write operator with doc comment`() {
        val operator = DartOperatorSpec.builder(UnaryOperator.NEGATE)
            .returnType(ClassName("Vector"))
            .doc("Returns the inverted vector.")
            .addCode("return %L;", "Vector(-x, -y)")
            .build()

        assertThat(operator.toString()).isEqualTo(
            """
            |/// Returns the inverted vector.
            |Vector operator -() {
            |  return Vector(-x, -y);
            |}
            """.trimMargin()
        )
    }
}
