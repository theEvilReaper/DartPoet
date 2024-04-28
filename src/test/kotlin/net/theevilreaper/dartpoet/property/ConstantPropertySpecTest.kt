package net.theevilreaper.dartpoet.property

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
        assertEquals(builder.initializer, newBuilder.initializer)
        assertFalse { newBuilder.isPrivate }
    }
}
