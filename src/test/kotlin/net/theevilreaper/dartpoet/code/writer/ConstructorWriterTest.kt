package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test constructor writer")
class ConstructorWriterTest {

    @Test
    fun `test constructor write without any special parameter`() {
        val constructor = ConstructorSpec.builder("Car")
            .parameters(
                ParameterSpec.builder("maker").build(),
                ParameterSpec.builder("model").build(),
                ParameterSpec.builder("yearMade").build(),
                ParameterSpec.builder("hasABS").build()
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
            .parameters(
                ParameterSpec.builder("maker").build(),
                ParameterSpec.builder("model").build(),
                ParameterSpec.builder("yearMade").build(),
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
            .parameters(
                ParameterSpec.builder("maker").build(),
                ParameterSpec.builder("model").build(),
                ParameterSpec.builder("yearMade").build(),
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
            .parameters(
                ParameterSpec.builder("maker").build(),
                ParameterSpec.builder("model").build(),
                ParameterSpec.builder("yearMade").build(),
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
            .parameters(
                ParameterSpec.builder("maker").required().build(),
                ParameterSpec.builder("model").named(true).build(),
                ParameterSpec.builder("yearMade").required().build(),
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Car({
              required this.maker,
              this.model,
              required this.yearMade
            });
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with named and variable with initializer`() {
        val constructor = ConstructorSpec.builder("Item")
            .parameters(
                ParameterSpec.builder("name").required().build(),
                ParameterSpec.builder("id").initializer("%L", 10L).build(),
                ParameterSpec.builder("amount").required().build()
            )
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            Item(this.id = 10,
            {
              required this.name,
              required this.amount
            });
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with documentation or comments`() {
        val constructor = ConstructorSpec.builder("Item")
            .doc("Creates a new item object")
            .parameters(
                ParameterSpec.builder("name").build())
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            /// Creates a new item object
            Item(this.name);
            """.trimIndent()
        )
    }
}
