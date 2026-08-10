package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.operator.UnaryOperator
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test operator generation as part of an extension")
class ExtensionWriterOperatorTest {

    @DartAnalyzeCase
    @Test
    fun `test extension with function and operator`() {
        val extension = ExtensionSpec.builder("ListExt", List::class.parameterizedBy(INTEGER))
            .function(
                FunctionSpec.builder("sum")
                    .returns(INTEGER)
                    .addCode("return fold(0, (a, b) => a + b);")
                    .build()
            )
            .operator(
                DartOperatorSpec.builder(UnaryOperator.NEGATE)
                    .returnType(List::class.parameterizedBy(INTEGER))
                    .addCode("return %L;", "reversed.toList()")
                    .build()
            )
            .build()

        assertThat(extension.toString()).isEqualTo(
            """
            |extension ListExt on List<int> {
            |  int sum() {
            |    return fold(0, (a, b) => a + b);
            |  }
            |
            |  List<int> operator -() {
            |    return reversed.toList();
            |  }
            |}
            """.trimMargin()
        )
    }
}
