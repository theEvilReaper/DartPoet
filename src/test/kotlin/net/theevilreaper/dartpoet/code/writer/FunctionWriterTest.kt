package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.MethodAccessorType
import net.theevilreaper.dartpoet.function.body.FunctionBodyType
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.FunctionTypeName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Type
import java.util.stream.Stream

@DisplayName("Test function writer")
class FunctionWriterTest {

    private companion object {

        @JvmStatic
        private fun bodyTypeCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                FunctionSpec.builder("getNameById")
                    .returns(ClassName("Future").parameterizedBy(STRING))
                    .body(FunctionBodyType.ASYNC)
                    .parameter(ParameterSpec.positional("id", INTEGER).build())
                    .addCode("return 'Thomas';")
                    .build(),
                """
                |Future<String> getNameById(int id) async {
                |  return 'Thomas';
                |}
                """.trimMargin()
            ),
            Arguments.of(
                FunctionSpec.builder("countTo")
                    .returns(ClassName("Iterable").parameterizedBy(INTEGER))
                    .body(FunctionBodyType.SYNC_STAR)
                    .parameter(ParameterSpec.positional("max", INTEGER).build())
                    .addCode(
                        buildCodeBlock {
                            beginControlFlow("for (var i = 1; i <= max; i++)")
                            addYield("i")
                            endControlFlow()
                        }
                    )
                    .build(),
                """
                |Iterable<int> countTo(int max) sync* {
                |  for (var i = 1; i <= max; i++) {
                |    yield i;
                |  }
                |}
                """.trimMargin()
            ),
            Arguments.of(
                FunctionSpec.builder("countAll")
                    .returns(ClassName("Stream").parameterizedBy(INTEGER))
                    .body(FunctionBodyType.ASYNC_STAR)
                    .parameter(ParameterSpec.positional("values", ClassName("Stream").parameterizedBy(INTEGER)).build())
                    .addCode(
                        buildCodeBlock {
                            addYield("0")
                            addYieldAll("values")
                        }
                    )
                    .build(),
                """
                |Stream<int> countAll(Stream<int> values) async* {
                |  yield 0;
                |  yield* values;
                |}
                """.trimMargin()
            ),
            Arguments.of(
                FunctionSpec.builder("values")
                    .returns(ClassName("Iterable").parameterizedBy(INTEGER))
                    .accessorType(MethodAccessorType.GETTER)
                    .body(FunctionBodyType.SYNC_STAR)
                    .addCode(
                        buildCodeBlock {
                            addYield("1")
                            addYield("2")
                        }
                    )
                    .build(),
                """
                |Iterable<int> get values sync* {
                |  yield 1;
                |  yield 2;
                |}
                """.trimMargin()
            ),
            Arguments.of(
                FunctionSpec.builder("value")
                    .returns(ClassName("Future").parameterizedBy(INTEGER))
                    .body(FunctionBodyType.ASYNC)
                    .type(FunctionType.SHORTEN)
                    .addCode("%L", "1;")
                    .build(),
                "Future<int> value() async => 1;"
            ),
            Arguments.of(
                FunctionSpec.builder("value")
                    .returns(ClassName("Future").parameterizedBy(INTEGER))
                    .accessorType(MethodAccessorType.GETTER)
                    .body(FunctionBodyType.ASYNC)
                    .addCode("return 1;")
                    .build(),
                """
                |Future<int> get value async {
                |  return 1;
                |}
                """.trimMargin()
            ),
            @Suppress("DEPRECATION")
            Arguments.of(
                FunctionSpec.builder("value")
                    .returns(INTEGER)
                    .accessorType(MethodAccessorType.GETTER)
                    .async(true)
                    .addCode("return 1;")
                    .build(),
                """
                |Future<int> get value async {
                |  return 1;
                |}
                """.trimMargin()
            ),
        )

        @JvmStatic
        private fun accessorSpacingCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                FunctionSpec.builder("value")
                    .returns(INTEGER)
                    .accessorType(MethodAccessorType.GETTER)
                    .addCode("return 1;")
                    .build(),
                """
                |int get value {
                |  return 1;
                |}
                """.trimMargin()
            ),
            Arguments.of(
                FunctionSpec.builder("value")
                    .accessorType(MethodAccessorType.SETTER)
                    .type(FunctionType.SHORTEN)
                    .parameter(ParameterSpec.positional("value", INTEGER).build())
                    .addCode("%L", "print(value);")
                    .build(),
                "set value(int value) => print(value);"
            ),
        )

        @JvmStatic
        private fun privateAccessorCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                FunctionSpec.builder("value")
                    .returns(INTEGER)
                    .modifier(DartModifier.PRIVATE)
                    .accessorType(MethodAccessorType.GETTER)
                    .type(FunctionType.SHORTEN)
                    .addCode("%L", "1;")
                    .build(),
                "int get _value => 1;"
            ),
            Arguments.of(
                FunctionSpec.builder("value")
                    .modifier(DartModifier.PRIVATE)
                    .accessorType(MethodAccessorType.SETTER)
                    .type(FunctionType.SHORTEN)
                    .parameter(ParameterSpec.positional("value", INTEGER).build())
                    .addCode("%L", "print(value);")
                    .build(),
                "set _value(int value) => print(value);"
            ),
        )

        @JvmStatic
        private fun genericOverloadCases(): Stream<Arguments> {
            val reflectType: Type = String::class.java
            return Stream.of(
                Arguments.of(
                    FunctionSpec.builder("identity")
                        .modifiers(DartModifier.EXTERNAL)
                        .generic(String::class)
                        .returns(ClassName("String"))
                        .parameter(ParameterSpec.positional("value", ClassName("String")).build())
                        .build(),
                    "external String identity<String>(String value);"
                ),
                Arguments.of(
                    FunctionSpec.builder("identity")
                        .modifiers(DartModifier.EXTERNAL)
                        .generic(reflectType)
                        .returns(ClassName("String"))
                        .parameter(ParameterSpec.positional("value", ClassName("String")).build())
                        .build(),
                    "external String identity<String>(String value);"
                ),
                Arguments.of(
                    FunctionSpec.builder("max")
                        .modifiers(DartModifier.EXTERNAL)
                        .generic("T", Comparable::class)
                        .returns(ClassName("T"))
                        .parameters(
                            ParameterSpec.positional("a", ClassName("T")).build(),
                            ParameterSpec.positional("b", ClassName("T")).build()
                        )
                        .build(),
                    "external T max<T extends Comparable>(T a, T b);"
                ),
            )
        }

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

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("genericOverloadCases")
    fun `test generic function overloads produce equivalent output`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test generic function write with multiple type parameters`() {
        val functionSpec = FunctionSpec.builder("map")
            .modifiers(DartModifier.EXTERNAL)
            .generic(ClassName("T"))
            .generic(ClassName("R"))
            .returns(ClassName("R"))
            .parameter(ParameterSpec.positional("value", ClassName("T")).build())
            .build()
        functionSpec.verifyDartOutput("external R map<T, R>(T value);")
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
    fun `test other getter variant write`() {
        val function = FunctionSpec.builder("value")
            .returns(Int::class)
            .type(FunctionType.SHORTEN)
            .accessorType(MethodAccessorType.GETTER)
            .addCode("%L", "_value;")
            .build()
        assertThat(function.toString()).isEqualTo("int get value => _value;")
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

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("bodyTypeCases")
    fun `test function write with body type`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("accessorSpacingCases")
    fun `test accessor write spacing`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("privateAccessorCases")
    fun `test private accessor write`(functionSpec: FunctionSpec, expected: String) {
        assertThat(functionSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test static generator function inside class`() {
        val classSpec = ClassSpec.builder("Counter")
            .function(
                FunctionSpec.builder("countTo")
                    .modifier(DartModifier.STATIC)
                    .returns(ClassName("Iterable").parameterizedBy(INTEGER))
                    .body(FunctionBodyType.SYNC_STAR)
                    .parameter(ParameterSpec.positional("max", INTEGER).build())
                    .addCode(
                        buildCodeBlock {
                            beginControlFlow("for (var i = 1; i <= max; i++)")
                            addYield("i")
                            endControlFlow()
                        }
                    )
                    .build()
            )
            .build()
        classSpec.verifyDartOutput(
            """
            |class Counter {
            |  static Iterable<int> countTo(int max) sync* {
            |    for (var i = 1; i <= max; i++) {
            |      yield i;
            |    }
            |  }
            |}
            """.trimMargin()
        )
    }
}
