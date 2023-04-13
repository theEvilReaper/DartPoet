package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeWriter

class FunctionWriterTest {

    @Test
    fun `write simple method without parameters`() {
        val writer = CodeWriter(StringBuilder())
        val method = DartFunctionSpec.builder("getName")
            .modifier(DartModifier.PUBLIC)
            .returns("String")
            .addCode("return %S;", "test")
            .build()
        writer.close()

        assertThat(method.toString()).isEqualTo(
            """
            String getName() {
              return ${"\""}test${"\""};
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `test simple private method`() {
        val writer = CodeWriter(StringBuilder())
        val method = DartFunctionSpec.builder("name")
            .returns("String")
            .modifier(DartModifier.PRIVATE)
            .addCode("return %S;", "Tobi").build()
        writer.close()
        assertThat(method.toString()).isEqualTo(
            """
            String _name() {
              return ${"\""}Tobi${"\""};
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `write simple nullable function`() {
        val method = DartFunctionSpec.builder("getId")
            .returns("int")
            .nullable(true)
            .addCode("return %L;", 10).build()
        assertThat(method.toString()).isEqualTo(
            """
            int? getId() {
              return 10;
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `write another nullable method`() {
        val method = DartFunctionSpec.builder("getValue")
            .returns("int?")
            .addCode("return 1;")
            .build()
        assertThat(method.toString()).isEqualTo(
            """
            int? getValue() {
              return 1;
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `write simple async function`() {

    }
}