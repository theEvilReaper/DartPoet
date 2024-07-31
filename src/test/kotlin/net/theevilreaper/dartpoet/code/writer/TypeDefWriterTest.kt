package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.DisplayName
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
                TypeDefSpec.builder("ValueUpdate", genericClassName)
                    .parameter(
                        ParameterSpec.builder("value", genericClassName)
                            .nullable(true)
                            .build()
                    )
                    .name("Function")
                    .build(),
                "typedef ValueUpdate<E> = void Function(E? value);"
            ),
            Arguments.of(
                TypeDefSpec.builder("json")
                    .returns(Map::class.parameterizedBy(String::class.asTypeName(), DYNAMIC))
                    .build(),
                "typedef json = Map<String, dynamic>;"
            ),
        )

        @JvmStatic
        private fun multipleCastArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                TypeDefSpec.builder(
                    "DoubleValueUpdate",
                    genericClassName, secondGenericClassName
                )
                    .name("Function")
                    .parameters(
                        ParameterSpec.builder("first", genericClassName)
                            .nullable(true)
                            .build(),
                        ParameterSpec.builder("second", secondGenericClassName)
                            .nullable(true)
                            .build()
                    )
                    .build(),
                "typedef DoubleValueUpdate<E, T> = void Function(E? first, T? second);"
            ),
            Arguments.of(
                TypeDefSpec.builder("Compare", genericClassName, secondGenericClassName)
                    .returns(Int::class)
                    .name("Function")
                    .parameters(
                        ParameterSpec.builder("a", genericClassName)
                            .build(),
                        ParameterSpec.builder("b", genericClassName)
                            .build()
                    )
                    .build(),
                "typedef Compare<E, T> = int Function(E a, E b);"
            )
        )

        @JvmStatic
        private fun differentParameterTypes(): Stream<Arguments> = Stream.of(
            Arguments.of(
                TypeDefSpec.builder("ValueUpdate", genericClassName)
                    .name("Function")
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.builder("value", String::class)
                            .build(),
                        ParameterSpec.optional("data", genericClassName)
                            .nullable(true)
                            .initializer("%L", "null")
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(String value, [E? data = null]);"
            ),
            Arguments.of(
                TypeDefSpec.builder("ValueUpdate", genericClassName)
                    .name("Function")
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.builder("map", Map::class.parameterizedBy(String::class, Int::class))
                            .build(),
                        ParameterSpec.optional("data", genericClassName)
                            .nullable(true)
                            .initializer("%L", "null")
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(Map<String, int> map, [E? data = null]);"
            ),
            Arguments.of(
                TypeDefSpec.builder("ValueUpdate", genericClassName)
                    .name("Function")
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.builder("list", List::class.parameterizedBy(String::class))
                            .build(),
                        ParameterSpec.required("data", genericClassName)
                            .build(),
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(List<String> list, {required E data});"
            ),
            Arguments.of(
                TypeDefSpec.builder("ValueUpdate", genericClassName)
                    .name("Function")
                    .returns(genericClassName)
                    .parameters(
                        ParameterSpec.builder("data", genericClassName)
                            .build(),
                        ParameterSpec.named("a", String::class).nullable(true).build(),
                        ParameterSpec.named("b", String::class).required().build(),
                        ParameterSpec.named("c", Int::class)
                            .initializer("%L", "10")
                            .build()
                    )
                    .build(),
                "typedef ValueUpdate<E> = E Function(E data, {required String b, String? a, int c = 10});"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("typeDefs")
    fun `test typedef write`(typeDef: TypeDefSpec, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("multipleCastArguments")
    fun `test typedef write with multiple casts`(typeDef: TypeDefSpec, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("differentParameterTypes")
    fun `test typedef write with different parameter types`(typeDef: TypeDefSpec, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }
}
