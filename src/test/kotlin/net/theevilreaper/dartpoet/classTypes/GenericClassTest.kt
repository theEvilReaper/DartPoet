package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Type
import java.util.stream.Stream

@DisplayName("Test the generation of classes which have generic arguments")
class GenericClassTest {

    companion object {
        @JvmStatic
        private fun genericOverloadCases(): Stream<Arguments> {
            val reflectType: Type = String::class.java
            return Stream.of(
                Arguments.of(ClassSpec.builder("TestClass").generic(String::class).build(), "class TestClass<String> {}"),
                Arguments.of(ClassSpec.builder("TestClass").generic(reflectType).build(), "class TestClass<String> {}"),
            )
        }

        @JvmStatic
        private fun boundedGenericCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                ClassSpec.builder("Box").generic("T", ClassName("Comparable")).build(),
                "class Box<T extends Comparable> {}"
            ),
            Arguments.of(
                ClassSpec.builder("Box")
                    .generic("T", ClassName("Comparable").parameterizedBy(ClassName("T")))
                    .build(),
                "class Box<T extends Comparable<T>> {}"
            ),
            Arguments.of(
                ClassSpec.builder("Box").generic("T", String::class).build(),
                "class Box<T extends String> {}"
            ),
            Arguments.of(
                ClassSpec.builder("Box")
                    .generic("T", ClassName("Comparable"))
                    .generic(ClassName("E"))
                    .build(),
                "class Box<T extends Comparable, E> {}"
            ),
        )
    }

    @Test
    fun testGenericClassTest() {
        val tClass = ClassName("T")
        val eClass = ClassName("E")
        val listClass: ParameterizedTypeName = List::class.parameterizedBy(eClass)
        val positionalParameter = ParameterSpec.positional("element", eClass).build()
        val genericClass: ClassSpec = ClassSpec.builder("TestClass")
            .generic(tClass)
            .generic(eClass)
            .property(
                PropertySpec.builder("argument", tClass)
                    .modifier { DartModifier.LATE }
                    .build()
            )
            .property(
                PropertySpec.builder("list", listClass)
                    .modifier { DartModifier.LATE }
                    .build()
            )
            .function(
                FunctionSpec.builder("add")
                    .parameter(positionalParameter)
                    .returns(Void::class)
                    .addCode("list.add(%N);", positionalParameter)
                    .build()
            )
            .build()
        genericClass.verifyDartOutput(
            """
            |class TestClass<T, E> {
            |
            |  late T argument;
            |  late List<E> list;
            |
            |  void add(E element) {
            |    list.add(element);
            |  }
            |}
            """.trimMargin()
        )
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("genericOverloadCases")
    fun `test generic class with KClass and Type overloads`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("boundedGenericCases")
    fun `test generic class with bounded type parameter`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }
}
