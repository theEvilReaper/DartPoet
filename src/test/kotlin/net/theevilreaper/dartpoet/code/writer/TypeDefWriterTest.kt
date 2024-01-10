package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DYNAMIC
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TypeDefWriterTest {

    companion object {

        private val genericClassName = ClassName("E")

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
            )
        )
    }

    @ParameterizedTest
    @MethodSource("typeDefs")
    fun `test typedef write`(typeDef: TypeDefSpec, expected: String) {
        Truth.assertThat(typeDef.toString()).isEqualTo(expected)
    }
}
