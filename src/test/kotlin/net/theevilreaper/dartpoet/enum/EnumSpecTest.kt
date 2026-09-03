package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.operator.BinaryOperator
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("Test EnumSpec generation and validation")
class EnumSpecTest {

    @Test
    fun `test simple enum generation`() {
        val enumSpec = EnumSpec.builder("Color")
            .entry(EnumEntrySpec.builder("red").build())
            .entry(EnumEntrySpec.builder("green").build())
            .entry(EnumEntrySpec.builder("blue").build())
            .build()

        enumSpec.verifyDartOutput(
            """
            |enum Color {
            |
            |  red,
            |  green,
            |  blue
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test enhanced enum generation`() {
        val enumSpec = EnumSpec.builder("NavigationEntry")
            .properties(
                PropertySpec.builder("name", String::class)
                    .modifier(DartModifier.FINAL)
                    .build(),
                PropertySpec.builder("route", String::class)
                    .modifier(DartModifier.FINAL)
                    .build()
            )
            .entries(
                EnumEntrySpec.builder("dashboard")
                    .parameter(EnumParameterSpec.positional("%C", "Dashboard"))
                    .parameter(EnumParameterSpec.positional("%C", "/dashboard"))
                    .build(),
                EnumEntrySpec.builder("build")
                    .parameter(EnumParameterSpec.positional("%C", "Build"))
                    .parameter(EnumParameterSpec.positional("%C", "/build"))
                    .build()
            )
            .constructor(
                ConstructorSpec.builder("NavigationEntry")
                    .modifier(DartModifier.CONST)
                    .parameter(ParameterSpec.positional("name").build())
                    .parameter(ParameterSpec.positional("route").build())
                    .build()
            )
            .build()

        enumSpec.verifyDartOutput(
            """
            |enum NavigationEntry {
            |
            |  dashboard('Dashboard', '/dashboard'),
            |  build('Build', '/build');
            |
            |  final String name;
            |  final String route;
            |
            |  const NavigationEntry(this.name, this.route);
            |
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test enum with mixin and interface`() {
        val enumSpec = EnumSpec.builder("Status")
            .withMixins(ClassName("M1"))
            .implements(ClassName("I1"))
            .entry(EnumEntrySpec.builder("active").build())
            .build()

        enumSpec.verifyDartOutput(
            """
            |enum Status with M1 implements I1 {
            |
            |  active
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test generic enum`() {
        val enumSpec = EnumSpec.builder("Result")
            .genericCast(ClassName("T"))
            .entry(EnumEntrySpec.builder("success").build())
            .entry(EnumEntrySpec.builder("failure").build())
            .build()

        enumSpec.verifyDartOutput(
            """
            |enum Result<T> {
            |
            |  success,
            |  failure
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test enum with annotations and private modifier`() {
        val enumSpec = EnumSpec.builder("Status", DartModifier.PRIVATE)
            .annotation(AnnotationSpec.builder("deprecated").build())
            .entry(EnumEntrySpec.builder("active").build())
            .build()

        assertEquals(
            """
            |@deprecated
            |enum _Status {
            |
            |  active
            |}
            """.trimMargin(),
            enumSpec.toString()
        )
    }

    @Test
    fun `test enum with endWithNewLine`() {
        val enumSpec = EnumSpec.builder("Status")
            .entry(EnumEntrySpec.builder("active").build())
            .endWithNewLine(true)
            .build()

        enumSpec.verifyDartOutput(
            """
            |enum Status {
            |
            |  active
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test enum inside DartFile`() {
        val dartFile = DartFile.builder("color_enum")
            .type(
                EnumSpec.builder("Color")
                    .entry(EnumEntrySpec.builder("red").build())
                    .entry(EnumEntrySpec.builder("green").build())
                    .build()
            )
            .build()

        dartFile.verifyDartOutput(
            """
            |enum Color {
            |
            |  red,
            |  green
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test toBuilder roundtrip`() {
        val original = EnumSpec.builder("Color")
            .entry(EnumEntrySpec.builder("red").build())
            .build()
        val modified = original.toBuilder()
            .entry(EnumEntrySpec.builder("blue").build())
            .build()

        assertEquals(1, original.entries.size)
        assertEquals(2, modified.entries.size)
        assertEquals("blue", modified.entries[1].name)
    }

    @Test
    fun `test validation empty entries throws`() {
        val exception = assertThrows<IllegalStateException> {
            EnumSpec.builder("EmptyEnum").build()
        }
        assertEquals("An enum requires at least one enum entry", exception.message)
    }

    @Test
    fun `test validation entry parameter count mismatch throws`() {
        val exception = assertThrows<IllegalStateException> {
            EnumSpec.builder("MismatchEnum")
                .property(PropertySpec.builder("name", String::class).build())
                .entry(
                    EnumEntrySpec.builder("test")
                        .parameter(EnumParameterSpec.positional("%C", "Test"))
                        .parameter(EnumParameterSpec.positional("%L", "10"))
                        .build()
                )
                .build()
        }
        assertEquals("The entries from the enum property must have the same size", exception.message)
    }

    @Test
    fun `test validation duplicate mixin throws`() {
        val exception = assertThrows<IllegalStateException> {
            EnumSpec.builder("DupMixin")
                .withMixins(ClassName("M1"), ClassName("M1"))
                .entry(EnumEntrySpec.builder("entry").build())
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate mixin type(s) found"))
    }

    @Test
    fun `test validation duplicate interface throws`() {
        val exception = assertThrows<IllegalStateException> {
            EnumSpec.builder("DupInterface")
                .implements(ClassName("I1"), ClassName("I1"))
                .entry(EnumEntrySpec.builder("entry").build())
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate interface type(s) found"))
    }

    @Test
    fun `test validation duplicate operator throws`() {
        val op1 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("int"))
            .parameter(ParameterSpec.positional("other", ClassName("int")).build())
            .addCode("return %L;", "1")
            .build()
        val op2 = DartOperatorSpec.builder(BinaryOperator.PLUS)
            .returnType(ClassName("int"))
            .parameter(ParameterSpec.positional("other", ClassName("int")).build())
            .addCode("return %L;", "2")
            .build()

        val exception = assertThrows<IllegalStateException> {
            EnumSpec.builder("DupOperator")
                .entry(EnumEntrySpec.builder("entry").build())
                .operator(op1)
                .operator(op2)
                .build()
        }
        assertTrue(exception.message!!.startsWith("Duplicate operator(s) found"))
    }
}
