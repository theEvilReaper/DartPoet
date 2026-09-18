package net.theevilreaper.dartpoet.extension.type

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DOUBLE
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Test ExtensionTypeSpec generation and validation")
class ExtensionTypeSpecTest {

    @Test
    fun `test simple extension type generation`() {
        val extensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .build()
        extensionType.verifyDartOutput("extension type Meters(double value) {}")
    }

    @Test
    fun `test const extension type with named constructor`() {
        val extensionType = ExtensionTypeSpec.builder("Id", ParameterSpec.positional("value", INTEGER).build())
            .constructorName("_")
            .const(true)
            .build()
        extensionType.verifyDartOutput("extension type const Id._(int value) {}")
    }

    @Test
    fun `test private extension type`() {
        val extensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .modifier(DartModifier.PRIVATE)
            .build()
        extensionType.verifyDartOutput("extension type _Meters(double value) {}")
    }

    @Test
    fun `test extension type with implements clause`() {
        val extensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .implements(ClassName("Comparable").parameterizedBy(ClassName("num")))
            .build()
        extensionType.verifyDartOutput("extension type Meters(double value) implements Comparable<num> {}")
    }

    @Test
    fun `test generic extension type`() {
        val extensionType = ExtensionTypeSpec.builder("Box", ParameterSpec.positional("value", ClassName("T")).build())
            .generic("T")
            .build()
        extensionType.verifyDartOutput("extension type Box<T>(T value) {}")
    }

    @Test
    fun `test extension type with functions, operators and static members`() {
        val extensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .constant(
                ConstantPropertySpec.classConst("zero", Double::class)
                    .initWith("%L", "0.0")
                    .build()
            )
            .property(
                PropertySpec.builder("unit", ClassName("String"))
                    .modifier { DartModifier.STATIC }
                    .initWith("%C", "m")
                    .build()
            )
            .function(
                FunctionSpec.builder("toFeet")
                    .returns(DOUBLE)
                    .addCode("return value * 3.281;")
                    .build()
            )
            .operator(
                DartOperatorSpec.builder(BinaryOperator.PLUS)
                    .returnType(ClassName("Meters"))
                    .parameter(ParameterSpec.positional("other", ClassName("Meters")).build())
                    .addCode("return Meters(value + other.value);")
                    .build()
            )
            .build()

        extensionType.verifyDartOutput(
            """
            |extension type Meters(double value) {
            |
            |  static const double zero = 0.0;
            |
            |  static String unit = 'm';
            |
            |  double toFeet() {
            |    return value * 3.281;
            |  }
            |
            |  Meters operator +(Meters other) {
            |    return Meters(value + other.value);
            |  }
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test extension type ends with new line`() {
        val extensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .endWithNewLine(true)
            .build()
        assertEquals("extension type Meters(double value) {}\n", extensionType.toString())
    }

    @Test
    fun `test validation throws on invalid name or modifier`() {
        val emptyEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("", ParameterSpec.positional("value", DOUBLE).build()).build()
        }
        assertEquals("The extension type name can not be empty or contain whitespaces", emptyEx.message)

        val modifierEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Invalid", ParameterSpec.positional("value", DOUBLE).build())
                .modifier(DartModifier.ABSTRACT)
                .build()
        }
        assertTrue(modifierEx.message!!.startsWith("An extension type can only have the PUBLIC or PRIVATE modifier"))
    }

    @Test
    fun `test validation throws on invalid representation parameter`() {
        val noTypeEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value").build()).build()
        }
        assertTrue(noTypeEx.message!!.contains("explicit type"))

        val namedEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Meters", ParameterSpec.named("value", DOUBLE).build()).build()
        }
        assertTrue(namedEx.message!!.contains("positional"))

        val coVariantEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder(
                "Meters",
                ParameterSpec.positional("value", DOUBLE).coVariant(true).build()
            ).build()
        }
        assertTrue(coVariantEx.message!!.contains("covariant"))

        val defaultValueEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder(
                "Meters",
                ParameterSpec.positional("value", DOUBLE).initializer("%L", "0.0").build()
            ).build()
        }
        assertTrue(defaultValueEx.message!!.contains("default value"))
    }

    @Test
    fun `test validation throws on duplicate interfaces or operators`() {
        val dupInterfaceEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
                .implements(ClassName("I1"), ClassName("I1"))
                .build()
        }
        assertTrue(dupInterfaceEx.message!!.startsWith("Duplicate interface type(s) found"))

        val op1 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("Meters"))
            .parameter(ParameterSpec.positional("other", ClassName("Meters")).build())
            .addCode("return this;")
            .build()
        val op2 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("Meters"))
            .parameter(ParameterSpec.positional("other", ClassName("Meters")).build())
            .addCode("return this;")
            .build()
        val dupOperatorEx = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
                .operator(op1)
                .operator(op2)
                .build()
        }
        assertTrue(dupOperatorEx.message!!.startsWith("Duplicate operator(s) found"))
    }

    @Test
    fun `test validation throws on non-static property`() {
        val ex = assertThrows<IllegalStateException> {
            ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
                .property(PropertySpec.builder("extra", DOUBLE).build())
                .build()
        }
        assertTrue(ex.message!!.contains("static"))
    }

    @Test
    fun `test toBuilder roundtrip`() {
        val original = ExtensionTypeSpec.builder("Original", ParameterSpec.positional("value", DOUBLE).build())
            .implements(ClassName("Comparable"))
            .generic("T")
            .function(FunctionSpec.builder("describe").build())
            .operator(
                DartOperatorSpec.builder(BinaryOperator.PLUS)
                    .returnType(DOUBLE)
                    .parameter(ParameterSpec.positional("other", DOUBLE).build())
                    .addCode("return value + other;")
                    .build()
            )
            .constant(
                ConstantPropertySpec.classConst("zero", Double::class)
                    .initWith("%L", "0.0")
                    .build()
            )
            .property(
                PropertySpec.builder("unit", ClassName("String"))
                    .modifier { DartModifier.STATIC }
                    .initWith("%C", "m")
                    .build()
            )
            .constructorName("_")
            .const(true)
            .endWithNewLine(true)
            .build()

        val modified = original.toBuilder()
            .function(FunctionSpec.builder("another").build())
            .build()

        assertEquals(1, original.functions.size)
        assertEquals(2, modified.functions.size)
        assertEquals(1, modified.interfaces.size)
        assertEquals(1, modified.genericCasts.size)
        assertEquals(1, modified.operators.size)
        assertEquals(1, modified.constants.size)
        assertEquals(1, modified.properties.size)
        assertEquals("_", modified.constructorName)
        assertTrue(modified.isConst)
        assertTrue(modified.endsWithNewLine)
    }

    @Test
    fun `test extension type inside DartFile`() {
        val meters = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .function(
                FunctionSpec.builder("toFeet")
                    .returns(DOUBLE)
                    .addCode("return value * 3.281;")
                    .build()
            )
            .build()

        val dartFile = DartFile.builder("meters")
            .type(meters)
            .build()

        dartFile.verifyDartOutput(
            """
            |extension type Meters(double value) {
            |
            |  double toFeet() {
            |    return value * 3.281;
            |  }
            |}
            """.trimMargin()
        )
    }
}
