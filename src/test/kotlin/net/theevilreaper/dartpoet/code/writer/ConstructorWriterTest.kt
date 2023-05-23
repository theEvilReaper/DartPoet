package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.*
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import org.junit.jupiter.api.Test

class ConstructorWriterTest {

    @Test
    fun `test constructor write without any special parameter`() {
        val constructor = ConstructorSpec.builder("Car")
            .parameters(
                DartParameterSpec.builder("maker").build(),
                DartParameterSpec.builder("model").build(),
                DartParameterSpec.builder("yearMade").build(),
                DartParameterSpec.builder("hasABS").build()
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
                DartParameterSpec.builder("maker").build(),
                DartParameterSpec.builder("model").build(),
                DartParameterSpec.builder("yearMade").build(),
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
                DartParameterSpec.builder("maker").build(),
                DartParameterSpec.builder("model").build(),
                DartParameterSpec.builder("yearMade").build(),
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
                DartParameterSpec.builder("maker").build(),
                DartParameterSpec.builder("model").build(),
                DartParameterSpec.builder("yearMade").build(),
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
                DartParameterSpec.builder("maker").required(true).build(),
                DartParameterSpec.builder("model").named(true).build(),
                DartParameterSpec.builder("yearMade").required(true).build(),
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
                DartParameterSpec.builder("name").required(true).build(),
                DartParameterSpec.builder("id").initializer("%L", 10L).build(),
                DartParameterSpec.builder("amount").required(true).build()
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
            .comment("Creates a new item object")
            .parameters(
                DartParameterSpec.builder("name").build())
            .build()
        assertThat(constructor.toString()).isEqualTo(
            """
            /// Creates a new item object
            Item(this.name);
            """.trimIndent()
        )
    }
}
