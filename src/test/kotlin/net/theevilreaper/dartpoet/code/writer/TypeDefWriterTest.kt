package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test typedef writer")
class TypeDefWriterTest {

    companion object {

        private val genericClassName = ClassName("E")
        private val secondGenericClassName = ClassName("T")

        @JvmStatic
        private fun typeDefs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                TypeDef.function("ValueUpdate", genericClassName)
                    .parameter(
                        ParameterSpec.positional("value", genericClassName)
                            .nullable(true)
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = void Function(E? value);"
            ),
            Arguments.of(
                TypeDef.alias("json")
                    .returns(Map::class.parameterizedBy(String::class.asTypeName(), DYNAMIC))
                    .build(),
                "typedef json = Map<String, dynamic>;"
            ),
        )

        @JvmStatic
        private fun multipleCastArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                TypeDef.function(
                    "DoubleValueUpdate",
                    genericClassName, secondGenericClassName
                )
                    .parameters(
                        ParameterSpec.positional("first", genericClassName)
                            .nullable(true)
                            .build(),
                        ParameterSpec.positional("second", secondGenericClassName)
                            .nullable(true)
                            .build()
                    )
                    .build(),
                "typedef DoubleValueUpdate<E, T> = void Function(E? first, T? second);"
            ),
            Arguments.of(
                TypeDef.function("Compare", genericClassName, secondGenericClassName)
                    .returns(Int::class)
                    .parameters(
                        ParameterSpec.positional("a", genericClassName)
                            .build(),
                        ParameterSpec.positional("b", genericClassName)
                            .build()
                    )
                    .build(),
                "typedef Compare<E, T> = int Function(E a, E b);"
            )
        )

        @JvmStatic
        private fun differentParameterTypes(): Stream<Arguments> = Stream.of(
            Arguments.of(
                TypeDef.function("ValueUpdate", genericClassName)
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.positional("value", String::class)
                            .build(),
                        ParameterSpec.optional("data", genericClassName)
                            .nullable(true)
                            .initializer("%L", "null")
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(String value, [E? data]);"
            ),
            Arguments.of(
                TypeDef.function("ValueUpdate", genericClassName)
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.positional("map", Map::class.parameterizedBy(String::class, Int::class))
                            .build(),
                        ParameterSpec.optional("data", genericClassName)
                            .nullable(true)
                            .initializer("%L", "null")
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(Map<String, int> map, [E? data]);"
            ),
            Arguments.of(
                TypeDef.function("ValueUpdate", genericClassName)
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.positional("list", List::class.parameterizedBy(String::class))
                            .build(),
                        ParameterSpec.required("data", genericClassName)
                            .build(),
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(List<String> list, {required E data});"
            ),
            Arguments.of(
                TypeDef.function("ValueUpdate", genericClassName)
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.positional("data", genericClassName)
                            .build(), //Positional aka nen normaler Param ohne spezielle Eigenschaften
                        ParameterSpec.named("a", String::class).nullable(true).build(),  // Named optional
                        ParameterSpec.required("b", String::class).build(),
                        ParameterSpec.named("c", Int::class)
                            .initializer("%L", "10")
                            .build() // Named optional weil nullable oder default
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(E data, {required String b, String? a, int c});"
            )
        )
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("typeDefs")
    fun `test typedef write`(typeDef: AbstractTypeDef<*>, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("multipleCastArguments")
    fun `test typedef write with multiple casts`(typeDef: AbstractTypeDef<*>, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("differentParameterTypes")
    fun `test typedef write with different parameter types`(typeDef: AbstractTypeDef<*>, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }

    @Test
    fun `test typedef and its implementing function in the same file`() {
        val file = DartFile.builder("callback_lib")
            .type(
                ClassSpec.anonymousClassBuilder()
                    .typedef(
                        TypeDef.function("Callback")
                            .parameters(
                                ParameterSpec.positional("message", String::class).build(),
                                ParameterSpec.optional("code", Int::class).nullable(true).build()
                            )
                            .build()
                    )
                    .build()
            )
            .type(
                ClassSpec.builder("Handler")
                    .function(
                        FunctionSpec.builder("onMessage")
                            .returns(Void::class)
                            .parameters(
                                ParameterSpec.positional("message", String::class).build(),
                                ParameterSpec.optional("code", Int::class)
                                    .nullable(true)
                                    .initializer("%L", "0")
                                    .build()
                            )
                            .addCode("code ??= 0;")
                            .build()
                    )
                    .build()
            )
            .build()
        file.verifyDartOutput(
            """
            |typedef Callback = void Function(String message, [int? code]);
            |
            |class Handler {
            |
            |  void onMessage(String message, [int? code = 0]) {
            |    code ??= 0;
            |  }
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test typedef with optional parameter that is neither nullable nor has an initializer throws`() {
        val exception = assertThrows<IllegalArgumentException> {
            TypeDef.function("ValueUpdate", genericClassName)
                .returns(genericClassName)
                .parameter(ParameterSpec.optional("data", genericClassName).build())
                .build()
        }
        Truth.assertThat(exception.message).isEqualTo("Optional parameters must be nullable or have an initializer")
    }
}
