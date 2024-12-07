package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.buildCodeBlock
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test the generation of classes which have generic arguments")
class GenericClassTest {

    @Test
    fun testGenericClassTest() {
        val tClass = ClassName("T")
        val eClass = ClassName("E")
        val listClass: ParameterizedTypeName = List::class.parameterizedBy(eClass)
        val genericClass: ClassSpec = ClassSpec.builder("TestClass")
            .generic(tClass)
            .generic(listClass)
            .property(
                PropertySpec.builder("argument", tClass)
                    .build()
            )
            .property(
                PropertySpec.builder("list", listClass)
                    .build()
            )
            .function(
                FunctionSpec.builder("add")
                    .parameters(
                        ParameterSpec.positional("element", eClass)
                            .build()
                    )
                    .returns(Void::class)
                    .addCode(
                        buildCodeBlock {
                            add("list.add(element);")
                        }
                    )
                    .build()
            )
            .build()
        Truth.assertThat(genericClass.toString()).isEqualTo(
            """
            |class TestClass<T, List<E>> {
            |
            |  T argument;
            |  List<E> list;
            |
            |  void add(E element) {
            |    list.add(element);
            |  }
            |}
            """.trimMargin()
        )
    }
}
