package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth
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

        Truth.assertThat(abstractClass.toString()).isEqualTo(
            """
            abstract class DatabaseHandler {
            
              TestModel getByID(int id);
            
              void test();
            
            }
            
            """.trimIndent()
        )

    }
}