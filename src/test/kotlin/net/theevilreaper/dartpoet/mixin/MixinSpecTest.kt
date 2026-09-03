package net.theevilreaper.dartpoet.mixin

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Test MixinSpec generation and validation")
class MixinSpecTest {

    @Test
    fun `test simple mixin generation`() {
        val mixinSpec = MixinSpec.builder("Walkable").build()
        mixinSpec.verifyDartOutput("mixin Walkable {}")
    }

    @Test
    fun `test base mixin generation`() {
        val mixinSpec = MixinSpec.builder("BaseWalkable")
            .modifier(DartModifier.BASE)
            .build()
        mixinSpec.verifyDartOutput("base mixin BaseWalkable {}")
    }

    @Test
    fun `test mixin with on clause`() {
        val mixinSpec = MixinSpec.builder("Walkable")
            .on(ClassName("Animal"), ClassName("Organism"))
            .build()
        assertEquals("mixin Walkable on Animal, Organism {}", mixinSpec.toString())
    }

    @Test
    fun `test mixin with implements clause`() {
        val mixinSpec = MixinSpec.builder("Walkable")
            .implements(ClassName("HasLegs"))
            .build()
        assertEquals("mixin Walkable implements HasLegs {}", mixinSpec.toString())
    }

    @Test
    fun `test mixin with both on and implements`() {
        val mixinSpec = MixinSpec.builder("Walkable")
            .on(ClassName("Animal"))
            .implements(ClassName("HasLegs"))
            .build()
        assertEquals("mixin Walkable on Animal implements HasLegs {}", mixinSpec.toString())
    }

    @Test
    fun `test mixin with properties, functions, and operators`() {
        val mixinSpec = MixinSpec.builder("Calculator")
            .property(
                PropertySpec.builder("value", ClassName("int"))
                    .initWith("%L", "0")
                    .build()
            )
            .function(
                FunctionSpec.builder("reset")
                    .addCode("// reset")
                    .build()
            )
            .operator(
                DartOperatorSpec.builder(BinaryOperator.PLUS)
                    .returnType(ClassName("int"))
                    .parameter(ParameterSpec.positional("other", ClassName("int")).build())
                    .addCode("return value + other;")
                    .build()
            )
            .build()

        mixinSpec.verifyDartOutput(
            """
            |mixin Calculator {
            |
            |  int value = 0;
            |
            |  void reset() {
            |    // reset
            |  }
            |
            |  int operator +(int other) {
            |    return value + other;
            |  }
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test generic mixin`() {
        val mixinSpec = MixinSpec.builder("Cache")
            .generic(ClassName("T"))
            .build()
        mixinSpec.verifyDartOutput("mixin Cache<T> {}")
    }

    @Test
    fun `test generic mixin with bounded type`() {
        val mixinSpec = MixinSpec.builder("Repository")
            .generic("T", ClassName("Entity"))
            .build()
        assertEquals("mixin Repository<T extends Entity> {}", mixinSpec.toString())
    }

    @Test
    fun `test mixin with constants`() {
        val mixinSpec = MixinSpec.builder("ConstantsMixin")
            .constant(
                ConstantPropertySpec.classConst("version", String::class)
                    .initWith("%C", "1.0.0")
                    .build()
            )
            .build()

        mixinSpec.verifyDartOutput(
            """
            |mixin ConstantsMixin {
            |
            |  static const String version = '1.0.0';
            |
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test mixin with typedef`() {
        val mixinSpec = MixinSpec.builder("TypedefMixin")
            .typeDef(
                TypeDef.alias("StringList")
                    .returns(ClassName("List"))
                    .build()
            )
            .build()

        assertEquals(
            """
            |mixin TypedefMixin {
            |
            |  typedef StringList = List;
            |
            |}
            """.trimMargin(),
            mixinSpec.toString()
        )
    }

    @Test
    fun `test mixin ends with new line`() {
        val mixinSpec = MixinSpec.builder("Trailing")
            .endWithNewLine(true)
            .build()
        assertEquals("mixin Trailing {}\n", mixinSpec.toString())
    }

    @Test
    fun `test private mixin`() {
        val mixinSpec = MixinSpec.builder("PrivateMixin")
            .modifier(DartModifier.PRIVATE)
            .build()
        mixinSpec.verifyDartOutput("mixin _PrivateMixin {}")
    }

    @Test
    fun `test mixin inside DartFile`() {
        val animal = ClassSpec.builder("Animal").build()
        val walkable = MixinSpec.builder("Walkable")
            .on(ClassName("Animal"))
            .function(
                FunctionSpec.builder("walk")
                    .addCode("// walk")
                    .build()
            )
            .build()
        val dog = ClassSpec.builder("Dog")
            .superClass(ClassName("Animal"))
            .withMixins(ClassName("Walkable"))
            .build()

        val dartFile = DartFile.builder("animals")
            .type(animal)
            .type(walkable)
            .type(dog)
            .build()

        dartFile.verifyDartOutput(
            """
            |class Animal {}
            |mixin Walkable on Animal {
            |
            |  void walk() {
            |    // walk
            |  }
            |}
            |class Dog extends Animal with Walkable {}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test mixin builder inside DartFile`() {
        val mixinBuilder = MixinSpec.builder("Flyable")
            .function(FunctionSpec.builder("fly").build())

        val dartFile = DartFile.builder("flying")
            .type(mixinBuilder.build())
            .build()

        dartFile.verifyDartOutput(
            """
            |mixin Flyable {
            |
            |  void fly();
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test validation blank or whitespace name throws`() {
        val exceptionEmpty = assertThrows<IllegalStateException> {
            MixinSpec.builder("").build()
        }
        assertEquals("The mixin name can not be empty or contain whitespaces", exceptionEmpty.message)

        val exceptionWhitespace = assertThrows<IllegalStateException> {
            MixinSpec.builder("Invalid Name").build()
        }
        assertEquals("The mixin name can not be empty or contain whitespaces", exceptionWhitespace.message)
    }

    @Test
    fun `test validation invalid modifier throws`() {
        val exception = assertThrows<IllegalStateException> {
            MixinSpec.builder("InvalidModifier")
                .modifier(DartModifier.ABSTRACT)
                .build()
        }
        assertTrue(exception.message!!.startsWith("A mixin can only have the 'base' modifier"))
    }

    @Test
    fun `test validation duplicate on types throws`() {
        val exception = assertThrows<IllegalStateException> {
            MixinSpec.builder("DupOn")
                .on(ClassName("Base"), ClassName("Base"))
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate 'on' type(s) found"))
    }

    @Test
    fun `test validation duplicate interfaces throws`() {
        val exception = assertThrows<IllegalStateException> {
            MixinSpec.builder("DupInterface")
                .implements(ClassName("I1"), ClassName("I1"))
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate interface type(s) found"))
    }

    @Test
    fun `test validation duplicate operators throws`() {
        val op1 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("int"))
            .parameter(ParameterSpec.positional("other", ClassName("int")).build())
            .addCode("return 1;")
            .build()
        val op2 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("int"))
            .parameter(ParameterSpec.positional("other", ClassName("int")).build())
            .addCode("return 2;")
            .build()

        val exception = assertThrows<IllegalStateException> {
            MixinSpec.builder("DupOperator")
                .operator(op1)
                .operator(op2)
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate operator(s) found"))
    }

    @Test
    fun `test toBuilder roundtrip`() {
        val original = MixinSpec.builder("Original")
            .on(ClassName("Animal"))
            .implements(ClassName("HasLegs"))
            .generic(ClassName("T"))
            .property(PropertySpec.builder("speed", ClassName("int")).build())
            .function(FunctionSpec.builder("move").build())
            .operator(
                DartOperatorSpec.builder(BinaryOperator.PLUS)
                    .returnType(ClassName("int"))
                    .parameter(ParameterSpec.positional("other", ClassName("int")).build())
                    .addCode("return speed + other;")
                    .build()
            )
            .constant(
                ConstantPropertySpec.classConst("maxSpeed", Int::class)
                    .initWith("%L", "100")
                    .build()
            )
            .typeDef(
                TypeDef.alias("SpeedList")
                    .returns(ClassName("List"))
                    .build()
            )
            .annotation(AnnotationSpec.builder("deprecated").build())
            .modifier(DartModifier.BASE)
            .endWithNewLine(true)
            .build()

        val modified = original.toBuilder()
            .property(PropertySpec.builder("distance", ClassName("int")).build())
            .build()

        assertEquals(1, original.properties.size)
        assertEquals(2, modified.properties.size)
        assertEquals(1, modified.onTypes.size)
        assertEquals(1, modified.interfaces.size)
        assertEquals(1, modified.genericCasts.size)
        assertEquals(1, modified.functions.size)
        assertEquals(1, modified.operators.size)
        assertEquals(1, modified.constants.size)
        assertEquals(1, modified.typeDefs.size)
        assertEquals(1, modified.annotations.size)
        assertTrue(modified.isBase)
        assertTrue(modified.endsWithNewLine)
    }
}
