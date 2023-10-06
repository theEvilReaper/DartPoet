package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ConstantPropertyWriterTest {

    companion object {

        @JvmStatic
        private fun testFileConstantWriting() = Stream.of(
            Arguments.of(
                "const String test = 'Test';",
                { ConstantPropertySpec.fileConst("test", String::class).initWith("%C", "Test").build() }
            ),
            Arguments.of(
                "const maxId = 100;",
                { ConstantPropertySpec.fileConst("maxId").initWith("%L", "100").build() }
            ),
            Arguments.of(
                "const List<String> strings = [];",
                {
                    ConstantPropertySpec.fileConst(
                        "strings",
                        List::class.asClassName().parameterizedBy(String::class.asTypeName())
                    ).initWith("[]").build()
                }
            ),
            Arguments.of(
                "const TestModel model = TestModel();",
                { ConstantPropertySpec.fileConst("model", ClassName("TestModel")).initWith("%L", "TestModel();").build() }
            )
        )

        @JvmStatic
        private fun testClassConstWriting() = Stream.of(
            Arguments.of(
                "static const String test = 'Test';",
                { ConstantPropertySpec.classConst("test", String::class).initWith("%C", "Test").build() }
            ),
            Arguments.of(
                "static const maxId = 100;",
                { ConstantPropertySpec.classConst("maxId").initWith("%L", "100").build() }
            ),
            Arguments.of(
                "static const List<String> strings = [];",
                {
                    ConstantPropertySpec.classConst(
                        "strings",
                        List::class.asClassName().parameterizedBy(String::class.asTypeName())
                    ).initWith("[]").build()
                }
            ),
            Arguments.of(
                "static const TestModel model = TestModel();",
                { ConstantPropertySpec.classConst("model", ClassName("TestModel")).initWith("%L", "TestModel()").build() }
            ),
            Arguments.of(
                "static const int _test = 1;",
                {
                    ConstantPropertySpec.classConst("test", Int::class)
                        .initWith("%L", "1")
                        .asPrivat(true)
                        .build()
                }
            )
        )
    }

    @ParameterizedTest
    @MethodSource("testFileConstantWriting")
    fun `test basic file const creation`(expected: String, block: () -> ConstantPropertySpec) {
        assertThat(block().toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("testClassConstWriting")
    fun `test basic class const creation`(expected: String, block: () -> ConstantPropertySpec) {
        assertThat(block().toString()).isEqualTo(expected)
    }
}