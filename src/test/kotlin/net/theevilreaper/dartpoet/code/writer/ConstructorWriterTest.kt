package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.minimized.MinimizedParameter
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test constructor writer")
class ConstructorWriterTest {

    @Test
    fun `test constructor write without any special parameter`() {
        val constructor = ConstructorSpec.builder("Car")
            .minis(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("maker").build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("model").build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("yearMade").build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("hasABS").build(),
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Car(this.maker, this.model, this.yearMade, this.hasABS);
            """.trimIndent()
        )
    }

    @Test
    fun `test named constructor write without any special parameter`() {
        val constructor = ConstructorSpec.named("Car", "withoutABS")
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("maker").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("model").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("yearMade").build(),
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Car.withoutABS(this.maker, this.model, this.yearMade);
            """.trimIndent()
        )
    }

    @Test
    fun `test named constructor2 write without any special parameter`() {
        val constructor = ConstructorSpec.named("Car", "withoutABS")
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("maker").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("model").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("yearMade").build(),
                )
            )
            .addCode(
                CodeBlock.of(
                    "hasABS = false"
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Car.withoutABS(this.maker, this.model, this.yearMade): hasABS = false;
            """.trimIndent()
        )
    }

    @Test
    fun `test const constructor without any parameter which has special properties`() {
        val constructor = ConstructorSpec.builder("Car")
            .modifier(DartModifier.CONST)
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("maker").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("model").build(),
                )
            )
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("yearMade").build(),
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            const Car(this.maker, this.model, this.yearMade);
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with required and named parameters`() {
        val constructor = ConstructorSpec.builder("Car")
            .minis(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("maker").build()
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.named("model").nullable(true).build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.required("yearMade").build(),
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Car({required this.maker, required this.yearMade, this.model});
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with named and variable with initializer`() {
        val constructor = ConstructorSpec.builder("Item")
            .minis(
                MinimizedParameter.fromParameter(
                    ParameterSpec.required("name").build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.named("id").initializer("%L", 10L).build(),
                ),
                MinimizedParameter.fromParameter(
                    ParameterSpec.required("amount").build()
                ),
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Item({required this.name, required this.amount, this.id = 10});
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with documentation or comments`() {
        val constructor = ConstructorSpec.builder("Item")
            .doc("Creates a new item object")
            .mini(
                MinimizedParameter.fromParameter(
                    ParameterSpec.positional("name").build()
                )
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            /// Creates a new item object
            Item(this.name);
            """.trimIndent()
        )
    }
}
