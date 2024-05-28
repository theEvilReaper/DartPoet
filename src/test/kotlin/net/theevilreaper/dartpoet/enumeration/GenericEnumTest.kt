package net.theevilreaper.dartpoet.enumeration

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test the generation of an enum which has a generic context")
class GenericEnumTest {

    private val genericEnum = "TestEnum"

    @Test
    fun testGenericEnumGeneration() {
        val genericClass = ClassName("T")
        val genericEnumSpec: ClassSpec = ClassSpec.enumClass(genericEnum)
            .generic(genericClass)
            .enumProperty(
                EnumPropertySpec.builder("name", genericClass)
                    .parameter("%C", "name")
                    .build()
            )
            .property(
                PropertySpec.builder("name", String::class)
                    .modifier(DartModifier.FINAL)
                    .build()
            )
            .constructor(
                ConstructorSpec.builder(genericEnum)
                    .modifier(DartModifier.CONST)
                    .parameter(
                        ParameterSpec.builder("name").build()
                    )
                    .build()
            )
            .build()
        Truth.assertThat(genericEnumSpec.toString()).isEqualTo(
            """
            enum $genericEnum<T> {
            
              name<T>('name');
            
              final String name;
            
              const $genericEnum(this.name);
            
            }
            """.trimIndent()
        )
    }
}