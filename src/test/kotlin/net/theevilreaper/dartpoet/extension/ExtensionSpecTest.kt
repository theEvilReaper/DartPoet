package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.operator.UnaryOperator
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test some ExtensionSpec object creations")
class ExtensionSpecTest {

    companion object {

        @JvmStatic
        private fun invalidExtensionSpecs() = Stream.of(
            Arguments.of(
                IllegalStateException::class.java,
                { ExtensionSpec.builder(EMPTY_STRING, String::class).build() },
                "The name of a extension can't be empty"
            ),
            Arguments.of(
                IllegalArgumentException::class.java,
                { ExtensionSpec.builder("StringExt", EMPTY_STRING).build() },
                "The name of a ClassName can't be empty (includes only spaces)"
            ),
            Arguments.of(
                IllegalStateException::class.java,
                {
                    ExtensionSpec.builder("ListExt", List::class.parameterizedBy(String::class))
                        .genericTypes(Int::class)
                        .build()
                },
                """
                The generic usage from the genericCast and extensionClass is not the same.
                Expected 'int' but got in the extension class: 'String'
                """.trimIndent()
            ),
            Arguments.of(
                IllegalStateException::class.java,
                {
                    ExtensionSpec.builder("MapExt", Map::class.parameterizedBy(String::class, Int::class))
                        .genericTypes(ClassName("D"), ClassName("D"))
                        .build()
                },
                """
                The generic usage from the genericCast and extensionClass is not the same.
                Expected 'D, D' but got in the extension class: 'String, int'
                """.trimIndent()
            )
        )
    }

    @ParameterizedTest(name = "Test invalid extension spec definitions")
    @MethodSource("invalidExtensionSpecs")
    fun `test invalid extension spec`(exception: Class<out Exception>, function: () -> Unit, message: String) {
        val givenException = assertThrows(exception) { function() }
        assertEquals(message, givenException.message)
    }

    @Test
    fun `test spec to builder conversation`() {
        val extensionSpec = ExtensionSpec.builder("isEmpty", "String")
            .endsWithNewLine(true)
            .doc("%C", "This is a test line")
            .operator(
                DartOperatorSpec.builder(UnaryOperator.NEGATE)
                    .returnType(net.theevilreaper.dartpoet.type.BOOLEAN)
                    .addCode("return %L;", "true")
                    .build()
            )
            .build()
        val specAsBuilder = extensionSpec.toBuilder()
        assertEquals(extensionSpec.name, specAsBuilder.name)
        assertEquals(extensionSpec.extClass, specAsBuilder.extClass)
        assertTrue { specAsBuilder.docs.isNotEmpty() }
        assertEquals(extensionSpec.endWithNewLine, specAsBuilder.endWithNewLine)
        assertEquals(extensionSpec.operators.toList(), specAsBuilder.operatorStack)
    }

    @Test
    fun `test duplicate operator symbol throws`() {
        assertThrows(IllegalStateException::class.java) {
            ExtensionSpec.builder("NumExt", Int::class)
                .operator(
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.positional("other", INTEGER).build())
                        .addCode("return %L;", "this + other")
                        .build()
                )
                .operator(
                    DartOperatorSpec.builder(BinaryOperator.PLUS)
                        .returnType(INTEGER)
                        .parameter(ParameterSpec.positional("other", INTEGER).build())
                        .addCode("return %L;", "this + other")
                        .build()
                )
                .build()
        }
    }

    @Test
    fun `test binary and unary operator sharing a symbol does not throw`() {
        val extension = ExtensionSpec.builder("VectorExt", ClassName("Vector"))
            .operator(
                DartOperatorSpec.builder(BinaryOperator.MINUS)
                    .returnType(ClassName("Vector"))
                    .parameter(ParameterSpec.positional("other", ClassName("Vector")).build())
                    .addCode("return %L;", "this")
                    .build()
            )
            .operator(
                DartOperatorSpec.builder(UnaryOperator.NEGATE)
                    .returnType(ClassName("Vector"))
                    .addCode("return %L;", "this")
                    .build()
            )
            .build()
        assertTrue { extension.toString().isNotEmpty() }
    }

    @Test
    fun `test extension with generic string overload`() {
        val extension = ExtensionSpec.builder("ListExt", List::class.parameterizedBy(ClassName("T")))
            .generic("T")
            .function(
                FunctionSpec.builder("firstOrNull")
                    .returns(ClassName("T").copy(nullable = true))
                    .addCode("return isEmpty ? null : first;")
                    .build()
            )
            .build()
        assertEquals(
            """
            |extension ListExt<T> on List<T> {
            |  T? firstOrNull() {
            |    return isEmpty ? null : first;
            |  }
            |}
            """.trimMargin(),
            extension.toString()
        )
    }
}
