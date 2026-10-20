package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.verify.GENERATED_SOURCE
import net.theevilreaper.dartpoet.verify.verifyDartOutputIn
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test constructor writer")
class ConstructorWriterTest {

    private companion object {
        val CAR = """
            |class Car {
            |  dynamic maker, model, yearMade, hasABS;
            |$GENERATED_SOURCE
            |}
            """.trimMargin()

        val CONST_CAR = """
            |class Car {
            |  final dynamic maker, model, yearMade;
            |$GENERATED_SOURCE
            |}
            """.trimMargin()

        val ITEM = """
            |class Item {
            |  dynamic name, amount, id;
            |$GENERATED_SOURCE
            |}
            """.trimMargin()

        val CAT = """
            |class Animal {
            |  final String name;
            |  Animal({required this.name});
            |}
            |
            |class Cat extends Animal {
            |  dynamic color;
            |$GENERATED_SOURCE
            |}
            """.trimMargin()

        val POINT = """
            |class Point {
            |$GENERATED_SOURCE
            |}
            """.trimMargin()
    }

    @Test
    fun `test constructor write without any special parameter`() {
        val constructor = ConstructorSpec.builder("Car")
            .parameters(
                ParameterSpec.positional("maker").build(),
                ParameterSpec.positional("model").build(),
                ParameterSpec.positional("yearMade").build(),
                ParameterSpec.positional("hasABS").build()
            )
            .build()
        constructor.verifyDartOutputIn(
            CAR,
            
            """
            Car(this.maker, this.model, this.yearMade, this.hasABS);
            """.trimIndent()
        )
    }

    @Test
    fun `test named constructor write without any special parameter`() {
        val constructor = ConstructorSpec.named("Car", "withoutABS")
            .parameters(
                ParameterSpec.positional("maker").build(),
                ParameterSpec.positional("model").build(),
                ParameterSpec.positional("yearMade").build(),
            )
            .build()
        constructor.verifyDartOutputIn(
            CAR,
            
            """
            Car.withoutABS(this.maker, this.model, this.yearMade);
            """.trimIndent()
        )
    }

    @Test
    fun `test named constructor2 write without any special parameter`() {
        val constructor = ConstructorSpec.named("Car", "withoutABS")
            .parameters(
                ParameterSpec.positional("maker").build(),
                ParameterSpec.positional("model").build(),
                ParameterSpec.positional("yearMade").build(),
            )
            .addCode(
                CodeBlock.of(
                    "hasABS = false"
                )
            )
            .build()
        constructor.verifyDartOutputIn(
            CAR,
            
            """
            Car.withoutABS(this.maker, this.model, this.yearMade) : hasABS = false;
            """.trimIndent()
        )
    }

    @Test
    fun `test const constructor without any parameter which has special properties`() {
        val constructor = ConstructorSpec.builder("Car")
            .modifier(DartModifier.CONST)
            .parameters(
                ParameterSpec.positional("maker").build(),
                ParameterSpec.positional("model").build(),
                ParameterSpec.positional("yearMade").build(),
            )
            .build()
        constructor.verifyDartOutputIn(
            CONST_CAR,
            
            """
            const Car(this.maker, this.model, this.yearMade);
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with required and named parameters`() {
        val constructor = ConstructorSpec.builder("Car")
            .parameters(
                ParameterSpec.required("maker").build(),
                ParameterSpec.named("model").nullable(true).build(),
                ParameterSpec.required("yearMade").build(),
            )
            .build()
        constructor.verifyDartOutputIn(
            CAR,
            
            """
            Car({required this.maker, required this.yearMade, this.model});
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with named and variable with initializer`() {
        val constructor = ConstructorSpec.builder("Item")
            .parameters(
                ParameterSpec.required("name").build(),
                ParameterSpec.named("id").initializer("%L", 10L).build(),
                ParameterSpec.required("amount").build()
            )
            .build()
        constructor.verifyDartOutputIn(
            ITEM,
            
            """
            Item({required this.name, required this.amount, this.id = 10});
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with documentation or comments`() {
        val constructor = ConstructorSpec.builder("Item")
            .doc("Creates a new item object")
            .parameters(
                ParameterSpec.positional("name").build())
            .build()
        constructor.verifyDartOutputIn(
            ITEM,
            
            """
            /// Creates a new item object
            Item(this.name);
            """.trimIndent()
        )
    }

    @Test
    fun `test constructor with super and field initializing parameters`() {
        val constructor = ConstructorSpec.builder("Cat")
            .parameters(
                ParameterSpec.required("name").superParameter().build(),
                ParameterSpec.named("color").nullable(true).build(),
            )
            .build()
        constructor.verifyDartOutputIn(
            CAT,
            
            """
            Cat({required super.name, this.color});
            """.trimIndent()
        )
    }

    @Test
    fun `test external constructor write`() {
        val constructor = ConstructorSpec.builder("Point")
            .modifier(DartModifier.EXTERNAL)
            .parameters(
                ParameterSpec.positional("x", Int::class).build(),
                ParameterSpec.positional("y", Int::class).build()
            )
            .build()
        constructor.verifyDartOutputIn(POINT, "external Point(int x, int y);")
    }

    @Test
    fun `test external named constructor write`() {
        val constructor = ConstructorSpec.named("Point", "origin")
            .modifier(DartModifier.EXTERNAL)
            .build()
        constructor.verifyDartOutputIn(POINT, "external Point.origin();")
    }
}
