package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@DisplayName("Test some EnumClass creations")
class EnumClassTest {

    companion object {

        @JvmStatic
        private fun invalidEnums() = Stream.of(
            Arguments.of(
                {
                    ClassSpec.enumClass("TestEnum")
                        .property(
                            PropertySpec.builder("name", String::class).build()
                        )
                        .build()
                },
                "A enum requires at least one enum property"
            ),
            Arguments.of(
                {
                    ClassSpec.enumClass("TestEnum")
                        .enumProperties(
                            EnumPropertySpec.builder("test")
                                .parameter("%C", "Test")
                                .parameter("%L", "10")
                                .build()
                        )
                        .property(PropertySpec.builder("name", String::class).build())
                        .constructor(
                            ConstructorSpec.builder("TestEnum")
                                .parameter(ParameterSpec.positional("name").build())
                                .build()
                        )
                        .build()
                },
                "The entries from the enum property must have the same size"
            )
        )
    }

    @ParameterizedTest(name = "Test cases for invalid enum definitions")
    @MethodSource("invalidEnums")
    fun `test invalid enum creation`(classSpec: () -> Unit, message: String) {
        val exception = assertThrows<IllegalStateException> { classSpec() }
        assertEquals(IllegalStateException::class, exception::class)
        assertEquals(message, exception.message)
    }

    @Test
    fun `test invalid enum creation`() {
        val enumClass = ClassSpec.enumClass("TestEnum")
            .property(PropertySpec.builder("name", String::class).build())
            .constructor(
                ConstructorSpec.builder("TestEnum")
                    .parameter(ParameterSpec.positional("name").build())
                    .build()
            )
        val exception = assertThrows<IllegalStateException> { enumClass.build() }
        assertEquals(IllegalStateException::class, exception::class)
        assertEquals("A enum requires at least one enum property", exception.message)
    }

    @Test
    fun `test enum class write`() {
        val enumClass = DartFile.builder("navigation_entry")
            .type(
                ClassSpec.enumClass("NavigationEntry")
                    .properties(
                        PropertySpec.builder("name", String::class)
                            .modifier { DartModifier.FINAL }.build(),
                        PropertySpec.builder("route", String::class)
                            .modifier { DartModifier.FINAL }.build()

                    )
                    .enumProperties(
                        EnumPropertySpec.builder("dashboard")
                            .parameter("%C", "Dashboard")
                            .parameter("%C", "/dashboard")
                            .build(),
                        EnumPropertySpec.builder("build")
                            .parameter("%C", "Build")
                            .parameter("%C", "/build")
                            .build()
                    )
                    .constructor(
                        ConstructorSpec.builder("NavigationEntry")
                            .modifier(DartModifier.CONST)
                            .parameters(
                                ParameterSpec.positional("name").build(),
                                ParameterSpec.positional("route").build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        assertThat(enumClass.toString()).isEqualTo(
            """
            enum NavigationEntry {
            
              dashboard('Dashboard', '/dashboard'),
              build('Build', '/build');
            
              final String name;
              final String route;
            
              const NavigationEntry(this.name, this.route);
            
            }
            """.trimIndent()
        )
    }

    @Test
    fun `test enum class with required parameters`() {
        val enumClass = ClassSpec.enumClass("Vehicle")
            .properties(
                PropertySpec.builder("tires", Int::class)
                    .modifier { DartModifier.FINAL }.build(),
                PropertySpec.builder("passengers", Int::class)
                    .modifier { DartModifier.FINAL }.build()

            )
            .enumProperty(
                EnumPropertySpec.builder("car")
                    .parameter("%L", 4)
                    .parameter("%L", 6)
                    .build()
            )
            .enumProperty(
                EnumPropertySpec.builder("bus")
                    .parameter("%L", 6)
                    .parameter("%L", 80)
                    .build()

            )
            .constructor(
                ConstructorSpec
                    .builder("Vehicle")
                    .modifier(DartModifier.CONST)
                    .parameter(
                        ParameterSpec.required("tires").build()
                    )
                    .parameter(
                        ParameterSpec.required("passengers").build()
                    )
                    .build()
            )
            .endWithNewLine(true)
            .build()

        assertThat(enumClass.toString()).isEqualTo(
            """
            enum Vehicle {
            
              car(tires: 4, passengers: 6),
              bus(tires: 6, passengers: 80);
            
              final int tires;
              final int passengers;
            
              const Vehicle({required this.tires, required this.passengers});
            
            }
            
            """.trimIndent()
        )
    }
}
