package net.theevilreaper.dartpoet.extension

import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
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
                { ExtensionSpec.builder("", String::class).build() },
                "The name of a extension can't be empty"
            ),
            Arguments.of(
                IllegalArgumentException::class.java,
                { ExtensionSpec.builder("StringExt", "").build() },
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
            .build()
        val specAsBuilder = extensionSpec.toBuilder()
        assertEquals(extensionSpec.name, specAsBuilder.name)
        assertEquals(extensionSpec.extClass, specAsBuilder.extClass)
        assertTrue { specAsBuilder.docs.isNotEmpty() }
        assertEquals(extensionSpec.endWithNewLine, specAsBuilder.endWithNewLine)
    }
}
