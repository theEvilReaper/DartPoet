package net.theevilreaper.dartpoet.code

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

/**
 * Dedicated test class which contains only test cases from a [CodeBlock] which uses the %N placeholder
 *
 * @see CodeBlock
 * @since 1.0.0
 * @author theEvilReaper
 */
class CodeBlockNameArgumentTest {

    @Test
    fun `test object which is not supported`() {
        val exception = assertThrows<IllegalArgumentException> {
            CodeBlock.builder()
                .add("%N", Any())
        }
        assertNotNull(exception)
        assertNotNull(exception.message)
        assertTrue { exception.message!!.contains("expected name but was java.lang.Object") }
    }

    @Test
    fun `test char sequence usage`() {
        val codeBlock = CodeBlock.builder()
            .add("%N", "Test")
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")

        val codeBlockString = codeBlock.toString()

        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertTrue(codeBlockString.contains("Test"), "The content should contain \"Test\"")
    }

    @Test
    fun `test parameter spec usage`() {
        val parameterSpec = ParameterSpec.named("test", String::class).build()
        val codeBlock = CodeBlock.builder()
            .add("%N = %C", parameterSpec, "Test")
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertThat(codeBlockString).isEqualTo("test = 'Test'")
    }

    @Test
    fun `test property spec usage`() {
        val property: PropertySpec = PropertySpec.builder("test", String::class).build()
        val codeBlock = CodeBlock.builder()
            .add("%N = %C", property, "Test")
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertThat(codeBlockString).isEqualTo("test = 'Test'")
    }

    @Test
    fun `test function spec usage`() {
        val function = FunctionSpec.builder("test").build()
        val codeBlock = CodeBlock.builder()
            .add("%N()", function)
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertNotNull(codeBlockString, "The content from the CodeBlock should not be null")
        assertThat(codeBlockString).isEqualTo("test()")
    }

    @Test
    fun `test class spec usage`() {
        val clazz = ClassSpec.builder("TestClass").build()
        val codeBlock = CodeBlock.builder()
            .add("%N()", clazz)
            .build()
        assertFalse(codeBlock.formatParts.isEmpty(), "The formats parts should be not empty")
        val codeBlockString = codeBlock.toString()
        assertThat(codeBlockString).isEqualTo("TestClass()")
    }

    @Test
    fun `test alias type spec usage`() {
        val typeDef = TypeDef.alias("StringList")
            .returns(List::class)
            .build()
        val function = FunctionSpec.builder("stringListUsage")
            .returns(List::class)
            .addCode(
                buildCodeBlock {
                    addStatement("%N a = ['Test1', 'Test2', 'Test3'];", typeDef)
                    add("return a;")
                }
            )
            .build()

        assertThat(function.toString()).isEqualTo(
            """
            |List stringListUsage() {
            |  StringList a = ['Test1', 'Test2', 'Test3'];
            |  return a;
            |}
        """.trimMargin()
        )
    }

    @Test
    fun `test function type spec usage`() {
        val typeDef = TypeDef.function("IntTaker")
            .returns(Int::class)
            .parameters(
                ParameterSpec.positional("a", Int::class).build(),
            )
            .build()
        val function = FunctionSpec.builder("intUsage")
            .returns(Int::class)
            .parameters(
                ParameterSpec.positional("a", Int::class).build()
            )
            .addCode(
                buildCodeBlock {
                    addStatement("%N a = (x) => x + 5;", typeDef)
                    add("return a(%L);", "10")
                }
            )
            .build()

        assertThat(function.toString()).isEqualTo(
            """
            |int intUsage(int a) {
            |  IntTaker a = (x) => x + 5;
            |  return a(10);
            |}
        """.trimMargin()
        )
    }
}
