package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.MethodAccessorType
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asClassName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test function writer")
class FunctionWriterTest {

    private companion object {

        @JvmStatic
        private fun castFunctionWrite() = Stream.of(
            Arguments.of(
                FunctionSpec.builder("getId").returns(Int::class).typeCast(Int::class).build(),
                "int getId<int>();"
            ),
            Arguments.of(
                FunctionSpec.builder("getModels").returns(List::class.parameterizedBy(ClassName("Model")))
                    .typeCast(List::class.parameterizedBy(DYNAMIC))
                    .build(),
                "List<Model> getModels<List<dynamic>>();",
            )
        )

        @JvmStatic
        private fun basicFunctionWrites(): Stream<Arguments> = Stream.of(
            Arguments.of(
                FunctionSpec.builder("test")
                    .returns(Void::class)
                    .build(),
                "void test();"
            ),
            Arguments.of(
                FunctionSpec.builder("getAllById")
                    .returns(List::class.parameterizedBy(ClassName("Model")))
                    .parameters(
                        ParameterSpec.builder("id", String::class).build(),
                        ParameterSpec.builder("amount", Int::class).build()
                    )
                    .build(),
                "List<Model> getAllById(String id, int amount);"
            ),
            Arguments.of(
                FunctionSpec.builder("test")
                    .returns(Void::class)
                    .parameters(
                        ParameterSpec.builder("id", String::class).nullable(true).build(),
                        ParameterSpec.builder("amount", Int::class).build()
                    )
                    .build(),
                "void test(String? id, int amount);"
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("castFunctionWrite")
    fun `test function write with cast typeNames`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("basicFunctionWrites")
    fun `test basic function write`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `write simple method without parameters`() {
        val writer = CodeWriter(StringBuilder())
        val method = FunctionSpec.builder("getName")
            .modifier(DartModifier.PUBLIC)
            .returns(String::class)
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
        val method = FunctionSpec.builder("name")
            .returns(String::class)
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
        val method = FunctionSpec.builder("getId")
            .returns(Int::class.asClassName().copy(nullable = true))
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
        val method = FunctionSpec.builder("getValue")
            .returns(Int::class.asClassName().copy(nullable = true))
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
        val method = FunctionSpec.builder("getNameById")
            .returns(String::class)
            .async(true)
            .parameter {
                ParameterSpec.builder("id", Int::class).build()
            }
            .addCode(
                CodeBlock.builder()
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
    fun `test other getter variant write`() {
        val function = FunctionSpec.builder("value")
            .returns(Int::class)
            .type(FunctionType.SHORTEN)
            .accessorType(MethodAccessorType.GETTER)
            .addCode("%L", "_value;")
            .build()
        assertThat(function.toString()).isEqualTo("int get value => _value;");
    }

    @Test
    fun `test other setter variant write`() {
        val function = FunctionSpec.builder("value")
            .parameter(
                ParameterSpec.builder("value", Int::class).build()
            )
            .accessorType(MethodAccessorType.SETTER)
            .addCode(
                buildCodeBlock {
                    add("%L = %L;", "_value", "value")
                }
            )
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
        val function = FunctionSpec.builder("isNoble")
            .parameter(ParameterSpec.builder("atomicNumber", Int::class).build())
            .type(FunctionType.SHORTEN)
            .returns(Boolean::class)
            .addCode("_nobleGases[atomicNumber] != null;")
            .build()
        assertThat(function.toString()).isEqualTo("bool isNoble(int atomicNumber) => _nobleGases[atomicNumber] != null;")
    }

    @Test
    fun `test method with documentation`() {
        val function = FunctionSpec.builder("getName")
            .returns(String::class)
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

    @Test
    fun `test function write with named and required parameters`() {
        val functionSpec = FunctionSpec.builder("testMethod")
            .modifiers(DartModifier.ABSTRACT)
            .parameters(
                ParameterSpec.named("a", String::class).nullable(true).build(),
                ParameterSpec.required("b", String::class).build(),
                ParameterSpec.named("c", Int::class)
                    .initializer("%L", "10")
                    .build()
            )
            .build()
        assertThat(functionSpec.toString()).isEqualTo(
            """
           abstract void testMethod({required String b, String? a, int c = 10});
            """.trimIndent()
        )
    }
}
