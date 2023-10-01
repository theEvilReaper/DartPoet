package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.file.FileConstantSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class FileConstWriterTest {

    companion object {

        @JvmStatic
        private fun constCreationTest() = Stream.of(
            Arguments.of(
                "const String test = \"Works\";",
                { FileConstantSpec.builder("test", String::class).initWith("%S", "Works").build() }
            )
        )
    }

    @ParameterizedTest
    @MethodSource("constCreationTest")
    fun `test basic file const creation`(expected: String, block: () -> FileConstantSpec) {
        assertEquals(expected, block().toString())
    }
}