package net.theevilreaper.dartpoet.property

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.IllegalArgumentException
import java.util.stream.Stream

@DisplayName("Test constant property spec creation")
class ConstantPropertySpecTest {

    companion object {

        @JvmStatic
        private fun invalidFileConstantSpec() = Stream.of(
            Arguments.of(
                "The name of a file constant can't be empty",
                { ConstantPropertySpec.classConst(EMPTY_STRING, String::class).build() }
            ),
            Arguments.of(
                "The initializer can't be empty",
                { ConstantPropertySpec.classConst("test", String::class).build() }
            ),
            Arguments.of(
                "A file constant can't be private",
                { ConstantPropertySpec.fileConst("test").initWith("%S", "test").private(true).build() }
            )
        )
    }

    @ParameterizedTest(name = "Test invalid file constant spec: {arguments}")
    @MethodSource("invalidFileConstantSpec")
    fun `test invalid file constant spec`(message: String, block: () -> ConstantPropertySpec) {
        val exception = assertThrows<IllegalArgumentException> { block() }
        assertEquals(message, exception.message)
    }

    @Test
    fun `test toBuilder`() {
        val builder = ConstantPropertySpec.classConst("test", String::class)
        builder.initializer.add("%S", "test")
        val spec = ConstantPropertySpec(builder)
        val newBuilder = spec.toBuilder()
        assertEquals(builder.name, newBuilder.name)
        assertEquals(builder.typeName, newBuilder.typeName)
        assertEquals(builder.initializer.build(), newBuilder.initializer.build())
        assertFalse { newBuilder.isPrivate }
    }

    @Test
    fun `test builder changes after build don't affect the spec`() {
        val builder = ConstantPropertySpec.fileConst("value", Int::class).initWith("%L", 1)
        val spec = builder.build()
        val expected = spec.toString()
        builder.initWith(" + %L", 2)
        assertEquals(expected, spec.toString())
    }

    @Test
    fun `test changes on converted builder don't affect the original spec`() {
        val spec = ConstantPropertySpec.fileConst("value", Int::class).initWith("%L", 1).build()
        val expected = spec.toString()
        spec.toBuilder().initWith(" + %L", 2)
        assertEquals(expected, spec.toString())
    }

    @Test
    fun `test external code block changes after build don't affect the spec`() {
        val codeBlock = CodeBlock.builder().add("%L", 1)
        val spec = ConstantPropertySpec.fileConst("value", Int::class).initWith(codeBlock).build()
        val expected = spec.toString()
        codeBlock.add(" + %L", 2)
        assertEquals(expected, spec.toString())
    }
}
