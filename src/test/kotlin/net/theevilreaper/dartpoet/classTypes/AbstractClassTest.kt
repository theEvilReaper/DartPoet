package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import org.junit.jupiter.api.Test

class AbstractClassTest {

    @Test
    fun `test simple abstract class`() {
        val abstractClass = DartClassSpec.abstractClass("DatabaseHandler")
            .endWithNewLine(true)
            .function(DartFunctionSpec.builder("getByID")
                .returns("TestModel")
                .parameter(DartParameterSpec.builder("id", "int").build())
                .build()
            )
            .function(DartFunctionSpec.builder("test").build())
            .build()

        assertThat(abstractClass.toString()).isEqualTo(
            """
            abstract class DatabaseHandler {
            
              TestModel getByID(int id);
            
              void test();
            
            }
            
            """.trimIndent()
        )

    }

    @Test
    fun `test abstract class with annotation`() {
        val abstractClass = DartClassSpec.abstractClass("Test")
            .annotation(
                AnnotationSpec.builder("abc").build()
            )
            .function(DartFunctionSpec.builder("test").build())
            .build()
        assertThat(abstractClass.toString()).isEqualTo(
            """
            @abc
            abstract class Test {
            
              void test();
            
            }
            """.trimIndent()
        )
    }
}