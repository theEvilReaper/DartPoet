package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

class FunctionWriterTest {

    @Test
    fun `write void method`() {
        val method = DartFunctionSpec.builder("test")
            .returns("void")
            .build()
        assertThat(method.toString()).isEqualTo("void test();")
    }

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
                .add("return 'Thomas';")
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

    @Test
    fun `test cast write`() {
        val function = DartFunctionSpec.builder("getId")
            .returns("int")
            .typeCast("int")
            .build()
        assertThat(function.toString()).isEqualTo("int getId<int>();")
    }

    @Test
    fun `test other getter variant write`() {
        val function = DartFunctionSpec.builder("value")
            .returns("int")
            .getter(true)
            .addCode("%L", "_value;")
            .build()
        assertThat(function.toString()).isEqualTo("int get value => _value;");
    }

    @Test
    fun `test other setter variant write`() {
        val function = DartFunctionSpec.builder("value")
            .parameter(
                DartParameterSpec.builder("value", "int")
                    .build()
            )
            .setter(true)
            .addCode(buildCodeBlock {
                add("%L = %L;", "_value", "value")
            })
            .build()
        assertThat(function.toString()).isEqualTo(
            """
            set value(int value) {
              _value = value;
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test lambda method write`() {
        val function = DartFunctionSpec.builder("isNoble")
            .lambda(true)
            .parameter(DartParameterSpec.builder("atomicNumber", "int").build())
            .returns("bool")
            .addCode("_nobleGases[atomicNumber] != null;")
            .build()
        assertThat(function.toString()).isEqualTo("bool isNoble(int atomicNumber) => _nobleGases[atomicNumber] != null;")
    }

    @Test
    fun `test method with documentation`() {
        val function = DartFunctionSpec.builder("getName")
            .returns("String")
            .addCode("return %C;", "Test")
            .doc("Returns the name from an object")
            .doc("For generation tests it returns 'Test'")
            .build()
        assertThat(function.toString()).isEqualTo(
            """
            /// Returns the name from an object
            /// For generation tests it returns 'Test'
            String getName() {
              return 'Test';
            }
            """.trimIndent()
        )
    }
}
