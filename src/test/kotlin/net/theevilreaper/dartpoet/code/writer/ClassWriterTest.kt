package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ClassWriterTest {

    companion object {

        @JvmStatic
        private fun simpleClasses() = Stream.of(
            Arguments.of(ClassSpec.builder("Test").build(), "class Test {}"),
            Arguments.of(ClassSpec.mixinClass("Test").build(), "mixin Test {}"),
            Arguments.of(ClassSpec.enumClass("Test").build(), "enum Test {}"),
            Arguments.of(
                ClassSpec.builder("Model").endWithNewLine(true).build(),
                """
                class Model {}
                
                """.trimIndent()
            ),
            Arguments.of(
                ClassSpec.abstractClass("DatabaseHandler").endWithNewLine(true).build(),
                """
                abstract class DatabaseHandler {}
                
                """.trimIndent()
            )
        )
    }

    @ParameterizedTest
    @MethodSource("simpleClasses")
    fun `test simple classes`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test class writing with some constants`() {
        val clazz = ClassSpec.builder("TestClass")
            .constants(
                ConstantPropertySpec.classConst("test", String::class)
                    .initWith("%C", "Test")
                    .build(),
                ConstantPropertySpec.classConst("maxId", Int::class)
                    .initWith("%L", "100")
                    .build(),
            )
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            class TestClass {
            
              static const String test = 'Test';
              static const int maxId = 100;
            
            }
            """.trimIndent()
        )
    }
}
