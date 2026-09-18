package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.extension.type.ExtensionTypeSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.BOOLEAN
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.OVERRIDE
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for ExtensionTypeSpec verified against the Dart analyzer")
class ExtensionTypeCorpusTest {

    @Test
    fun `test extension type implementing an interface used in a top-level function`() {
        val userId = ExtensionTypeSpec.builder("UserId", ParameterSpec.positional("value", INTEGER).build())
            .implements(ClassName("Comparable").parameterizedBy(ClassName("UserId")))
            .function(
                FunctionSpec.builder("compareTo")
                    .annotation(OVERRIDE)
                    .returns(INTEGER)
                    .parameter(ParameterSpec.positional("other", ClassName("UserId")).build())
                    .addCode("return value.compareTo(other.value);")
                    .build()
            )
            .build()

        val isSameUserFunc = FunctionSpec.builder("isSameUser")
            .returns(BOOLEAN)
            .parameter(ParameterSpec.positional("a", ClassName("UserId")).build())
            .parameter(ParameterSpec.positional("b", ClassName("UserId")).build())
            .addCode("return a == b;")
            .build()

        val file = DartFile.builder("user_id_corpus")
            .function(isSameUserFunc)
            .type(userId)
            .build()

        file.verifyDartOutput(
            """
            |bool isSameUser(UserId a, UserId b) {
            |  return a == b;
            |}
            |
            |extension type UserId(int value) implements Comparable<UserId> {
            |
            |  @override
            |  int compareTo(UserId other) {
            |    return value.compareTo(other.value);
            |  }
            |}
            """.trimMargin()
        )
    }

    @Test
    fun `test private const generic extension type with a named representation constructor`() {
        val wrapper = ExtensionTypeSpec.builder("Wrapper", ParameterSpec.positional("value", ClassName("T")).build())
            .modifier(DartModifier.PRIVATE)
            .constructorName("_")
            .const(true)
            .generic("T")
            .function(
                FunctionSpec.builder("unwrap")
                    .returns(ClassName("T"))
                    .addCode("return value;")
                    .build()
            )
            .build()

        val file = DartFile.builder("wrapper_corpus")
            .type(wrapper)
            .build()

        file.verifyDartOutput(
            """
            |extension type const _Wrapper._<T>(T value) {
            |
            |  T unwrap() {
            |    return value;
            |  }
            |}
            """.trimMargin()
        )
    }
}
