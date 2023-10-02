package net.theevilreaper.dartpoet.file

import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.IllegalArgumentException
import java.lang.reflect.Field
import java.util.stream.Stream


class FileConstantSpecTest {

    companion object {

        @JvmStatic
        private fun invalidFileConstantSpec() = Stream.of(
            Arguments.of(
                "The name of a file constant can't be empty",
                { FileConstantSpec.builder("", String::class).build() }
            ),
            Arguments.of(
                "The initializer can't be empty",
                { FileConstantSpec.builder("test", String::class).build() }
            )
        )

        @JvmStatic
        private fun testFileConstantCreation() = Stream.of(
            Arguments.of(
                "const Any test = Any();",
                { FileConstantSpec.builder("test", Any::class).initWith("%T()", Any::class).build() }
            ),
            Arguments.of(
                "const int value = 12;",
                { FileConstantSpec.builder("value", Int::class.asClassName()).initWith("%L", "12").build() }
            ),
            Arguments.of(
                "const typeDef = 1;",
                { FileConstantSpec.builder("typeDef").initWith("%L", "1").build() }
            ),
            Arguments.of(
                "const Field test = null;",
                { FileConstantSpec.builder("test", Field::class.asTypeName()).initWith("null").build() }
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidFileConstantSpec")
    fun `test invalid file constant spec`(message: String, block: () -> FileConstantSpec) {
        val exception = assertThrows<IllegalArgumentException> { block() }
        assertEquals(message, exception.message)
    }

    @ParameterizedTest
    @MethodSource("testFileConstantCreation")
    fun `test valid file const property generation`(expected: String, block: () -> FileConstantSpec) {
        assertEquals(expected, block().toString())
    }

    @Test
    fun `test toBuilder`() {
        val builder = FileConstantSpec.builder("test", String::class)
        builder.initializer.add("%S", "test")
        val spec = FileConstantSpec(builder)
        val newBuilder = spec.toBuilder()
        assertEquals(builder.name, newBuilder.name)
        assertEquals(builder.typeName, newBuilder.typeName)
        assertEquals(builder.initializer, newBuilder.initializer)
    }
}