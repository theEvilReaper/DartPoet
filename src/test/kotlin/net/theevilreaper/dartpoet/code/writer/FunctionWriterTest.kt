package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class FunctionWriterTest {

    @Test
    fun `write simple method without parameters`() {
        val writer = CodeWriter(StringBuilder())
        val method = DartFunctionSpec.builder("getName")
            .modifier(DartModifier.PUBLIC)
            .returns("String")
            .addCode("return %C;", "test")
            .build()
        writer.close()

        assertThat(method.toString()).isEqualTo(
            """
            String getName() {
              return 'test';
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
            .addCode("return %C;", "Tobi").build()
        writer.close()
        assertThat(method.toString()).isEqualTo(
            """
            String _name() {
              return 'Tobi';
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
        val method = DartFunctionSpec.builder("getNameById")
            .returns("String")
            .async(true)
            .parameter {
                DartParameterSpec.builder("id", "int").build()
            }
            .addCode(CodeBlock.builder()
                .addStatement("return 'Thomas';")
                .build()
            )
            .build()
        assertThat(method.toString()).isEqualTo(
            """
            Future<String> getNameById(int id) async {
              return 'Thomas';
            }
            """.trimIndent()
        )
    }

    @Test
    fun `write method with two parameters`() {
        val method = DartFunctionSpec.builder("getAllById")
            .returns("List<Model>")
            .parameters {
                listOf(
                    DartParameterSpec.builder("id", "String").build(),
                    DartParameterSpec.builder("amount", "int").build()
                )
            }
            .build()
        assertThat(method.toString()).isEqualTo(
            """
            List<Model> getAllById(String id, int amount);
            """.trimIndent()
        )
    }

    @Test
    fun `test typedef write`() {
        val function = DartFunctionSpec.builder("ValueUpdate<E>")
            .typedef(true)
            .parameter(DartParameterSpec.builder("value", "E")
                .nullable(true).build())
            .returns("void Function")
            .build()
        assertThat(function.toString()).isEqualTo("typedef ValueUpdate<E> = void Function(E? value);")
    }
}
