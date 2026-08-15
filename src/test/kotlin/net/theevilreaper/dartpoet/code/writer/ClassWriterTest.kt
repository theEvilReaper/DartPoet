package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.verify.DartAnalyzeCase
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName(value = "Test the class writer")
class ClassWriterTest {

    companion object {

        @JvmStatic
        private fun simpleClasses() = Stream.of(
            Arguments.of(ClassSpec.builder("Test").build(), "class Test {}"),
            Arguments.of(ClassSpec.mixinClass("Test").build(), "mixin Test {}"),
            Arguments.of(
                ClassSpec.builder("Model").endWithNewLine(true).build(),
                """
                class Model {}
                
                """.trimIndent()
            ),
            Arguments.of(
                ClassSpec.builder("PrivateClass").modifier { DartModifier.PRIVATE }.build(),
                "class _PrivateClass {}"
            ),
            Arguments.of(
                ClassSpec.builder("_AlreadyPrivate").modifier { DartModifier.PRIVATE }.build(),
                "class _AlreadyPrivate {}"
            ),
            Arguments.of(
                ClassSpec.abstractClass("DatabaseHandler").endWithNewLine(true).build(),
                """
                |abstract class DatabaseHandler {}
                |
                """.trimMargin()
            )
        )
    }

    @DartAnalyzeCase
    @ParameterizedTest
    @MethodSource("simpleClasses")
    fun `test simple classes`(classSpec: ClassSpec, expected: String) {
        assertThat(classSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test class writing with some constants`() {
        val clazz = ClassSpec.builder("TestClass")
            .constants(
                ConstantPropertySpec.classConst("test", String::class)
                    .initWith("%C", "Test")
                    .build(),
                ConstantPropertySpec.classConst("maxId", Int::class)
                    .initWith("%L", "100")
                    .build(),
            )
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            |class TestClass {
            |
            |  static const String test = 'Test';
            |  static const int maxId = 100;
            |
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test class writing with a typedef emits the typedef in its body`() {
        val clazz = ClassSpec.builder("TestClass")
            .typedef(
                TypeDef.alias("JsonMap")
                    .returns(ClassName("Map"))
                    .build()
            )
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            |class TestClass {
            |
            |  typedef JsonMap = Map;
            |
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test class writing with external members`() {
        val clazz = ClassSpec.builder("NativeBinding")
            .constructor(
                ConstructorSpec.builder("NativeBinding")
                    .modifier(DartModifier.EXTERNAL)
                    .build()
            )
            .property(
                PropertySpec.builder("nativeHandle", Int::class)
                    .modifier(DartModifier.EXTERNAL)
                    .build()
            )
            .function(
                FunctionSpec.builder("nativeCall", Int::class)
                    .modifier(DartModifier.EXTERNAL)
                    .parameters(ParameterSpec.positional("arg", String::class).build())
                    .build()
            )
            .build()

        clazz.verifyDartOutput(
            """
            |class NativeBinding {
            |
            |  external int nativeHandle;
            |
            |  external NativeBinding();
            |
            |  external int nativeCall(String arg);
            |}
            """.trimMargin()
        )
    }
}
