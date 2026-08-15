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
import net.theevilreaper.dartpoet.type.FunctionTypeName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
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
                        ParameterSpec.positional("id", String::class).build(),
                        ParameterSpec.positional("amount", Int::class).build()
                    )
                    .build(),
                "List<Model> getAllById(String id, int amount);"
            ),
            Arguments.of(
                FunctionSpec.builder("test")
                    .returns(Void::class)
                    .parameters(
                        ParameterSpec.positional("id", String::class).nullable(true).build(),
                        ParameterSpec.positional("amount", Int::class).build()
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

        method.verifyDartOutput(
            """
            |String getName() {
            |  return 'test';
            |}
            """.trimMargin()
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
        method.verifyDartOutput(
            """
            |String _name() {
            |  return 'Tobi';
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write simple nullable function`() {
        val method = FunctionSpec.builder("getId")
            .returns(Int::class.asClassName().copy(nullable = true))
            .addCode("return %L;", 10).build()
        method.verifyDartOutput(
            """
            |int? getId() {
            |  return 10;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write another nullable method`() {
        val method = FunctionSpec.builder("getValue")
            .returns(Int::class.asClassName().copy(nullable = true))
            .addCode("return 1;")
            .build()
        method.verifyDartOutput(
            """
            |int? getValue() {
            |  return 1;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `write simple async function`() {
        val method = FunctionSpec.builder("getNameById")
            .returns(String::class)
            .async(true)
            .parameter {
                ParameterSpec.positional("id", Int::class).build()
            }
            .addCode(
                CodeBlock.builder()
                    .add("return 'Thomas';")
                    .build()
            )
            .build()
        method.verifyDartOutput(
            """
            |Future<String> getNameById(int id) async {
            |  return 'Thomas';
            |}
            """.trimMargin()
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
                ParameterSpec.positional("value", Int::class).build()
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
            |set value(int value) {
            |  _value = value;
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test lambda method write`() {
        val function = FunctionSpec.builder("isNoble")
            .parameter(ParameterSpec.positional("atomicNumber", Int::class).build())
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
        function.verifyDartOutput(
            """
            |/// Returns the name from an object
            |/// For generation tests it returns 'Test'
            |String getName() {
            |  return 'Test';
            |}
            """.trimMargin()
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

    @Test
    fun `test function write with function type parameter`() {
        val functionSpec = FunctionSpec.builder("doWork")
            .parameter(
                ParameterSpec.positional(
                    "mapper",
                    FunctionTypeName.builder()
                        .returns(INTEGER)
                        .parameter(ParameterSpec.positional("value", STRING).build())
                        .build()
                ).build()
            )
            .addCode("mapper('test');")
            .build()
        functionSpec.verifyDartOutput(
            """
            |void doWork(int Function(String value) mapper) {
            |  mapper('test');
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test external function write`() {
        val functionSpec = FunctionSpec.builder("nativeMethod")
            .modifiers(DartModifier.EXTERNAL)
            .returns(INTEGER)
            .parameters(ParameterSpec.positional("count", INTEGER).build())
            .build()
        functionSpec.verifyDartOutput("external int nativeMethod(int count);")
    }

    @Test
    fun `test external getter and setter write`() {
        val getterSpec = FunctionSpec.builder("count", INTEGER)
            .modifiers(DartModifier.EXTERNAL)
            .accessorType(MethodAccessorType.GETTER)
            .build()
        getterSpec.verifyDartOutput("external int get count;")

        val setterSpec = FunctionSpec.builder("count", INTEGER)
            .modifiers(DartModifier.EXTERNAL)
            .accessorType(MethodAccessorType.SETTER)
            .parameter(ParameterSpec.positional("value", INTEGER).build())
            .build()
        setterSpec.verifyDartOutput("external set count(int value);")
    }
}
