package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ClassWriterTest {

    companion object {

        @JvmStatic
        private fun simpleClasses() = Stream.of(
            Arguments.of(DartClassSpec.builder("Test").build(), "class Test {}"),
            Arguments.of(DartClassSpec.mixinClass("Test").build(), "mixin Test {}"),
            Arguments.of(DartClassSpec.enumClass("Test").build(), "enum Test {}"),
            Arguments.of(
                DartClassSpec.builder("Model").endWithNewLine(true).build(),
                """
                class Model {}
                
                """.trimIndent()
            ),
            Arguments.of(
                DartClassSpec.abstractClass("DatabaseHandler").endWithNewLine(true).build(),
                """
                abstract class DatabaseHandler {}
                
                """.trimIndent()
            )
        )
    }

    @ParameterizedTest
    @MethodSource("simpleClasses")
    fun `test simple classes`(classSpec: DartClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test class writing with some constants`() {
        val clazz = DartClassSpec.builder("TestClass")
            .constants(
                DartPropertySpec.builder("test", "String")
                    .modifiers { listOf(DartModifier.STATIC, DartModifier.CONST) }
                    .initWith("%C", "Test")
                    .build(),
                DartPropertySpec.builder("maxId", "int")
                    .modifiers { listOf(DartModifier.CONST, DartModifier.STATIC) }
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
