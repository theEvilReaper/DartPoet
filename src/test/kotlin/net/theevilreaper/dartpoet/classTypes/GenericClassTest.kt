package net.theevilreaper.dartpoet.classTypes

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.reflect.Type

@DisplayName("Test the generation of classes which have generic arguments")
class GenericClassTest {

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

    @Test
    fun `test generic class with KClass overload`() {
        val genericClass = ClassSpec.builder("TestClass")
            .generic(String::class)
            .build()
        genericClass.verifyDartOutput("class TestClass<String> {}")
    }

    @Test
    fun `test generic class with reflect Type overload`() {
        val reflectType: Type = String::class.java
        val genericClass = ClassSpec.builder("TestClass")
            .generic(reflectType)
            .build()
        genericClass.verifyDartOutput("class TestClass<String> {}")
    }
}
