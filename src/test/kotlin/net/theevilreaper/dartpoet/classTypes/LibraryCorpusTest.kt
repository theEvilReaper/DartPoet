package net.theevilreaper.dartpoet.classTypes

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Bundles the generated Dart output of library classes that is verified against a real
 * Dart toolchain via [verifyDartOutput] / `dartAnalyzeCorpus`. Behavior and exception
 * cases live in [LibraryClassTest]; this class is only for rendered-output verification.
 */
@DisplayName("Test the library class type against a real Dart toolchain")
class LibraryCorpusTest {

    @Test
    fun `test library class renders its keyword and name`() {
        val libraryClass = ClassSpec.libraryClass("Foo").build()
        libraryClass.verifyDartOutput("library Foo;")
    }

    @Test
    fun `test library class type combined with a real class in the same file`() {
        val file = DartFile.builder("point_lib")
            .type(ClassSpec.libraryClass("PointLibrary").build())
            .type(
                ClassSpec.builder("Point")
                    .properties(
                        PropertySpec.builder("x", Int::class).build(),
                        PropertySpec.builder("y", Int::class).build(),
                    )
                    .constructor(
                        ConstructorSpec.builder("Point")
                            .parameters(
                                ParameterSpec.positional("x").build(),
                                ParameterSpec.positional("y").build(),
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        file.verifyDartOutput(
            """
            |library PointLibrary;
            |class Point {
            |
            |  int x;
            |  int y;
            |
            |  Point(this.x, this.y);
            |
            |}
            |
            """.trimMargin()
        )
    }
}
