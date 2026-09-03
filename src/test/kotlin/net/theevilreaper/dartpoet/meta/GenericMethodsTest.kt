package net.theevilreaper.dartpoet.meta

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.clazz.ClassType
import net.theevilreaper.dartpoet.enum.EnumBuilder
import net.theevilreaper.dartpoet.mixin.MixinBuilder
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeVariableName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Type
import java.util.stream.Stream

@DisplayName("Test GenericMethods overloads across all type builders")
class GenericMethodsTest {

    companion object {
        @JvmStatic
        fun builders(): Stream<GenericMethods<*>> = Stream.of(
            ClassBuilder("TestClass", ClassType.CLASS),
            EnumBuilder("TestEnum"),
            MixinBuilder("TestMixin")
        )
    }

    private fun getGenerics(builder: GenericMethods<*>): List<String> = when (builder) {
        is ClassBuilder -> builder.genericCasts.map { TypeVariableName.renderDeclaration(it) }
        is EnumBuilder -> builder.genericCasts.map { TypeVariableName.renderDeclaration(it) }
        is MixinBuilder -> builder.genericCasts.map { TypeVariableName.renderDeclaration(it) }
        else -> error("Unknown builder: $builder")
    }

    @ParameterizedTest
    @MethodSource("builders")
    fun `test unconstrained string generic parameter`(builder: GenericMethods<*>) {
        builder.generic("T")
        assertThat(getGenerics(builder)).containsExactly("T")
    }

    @ParameterizedTest
    @MethodSource("builders")
    fun `test type conversion overloads`(builder: GenericMethods<*>) {
        val reflectType: Type = String::class.java
        builder.generic(ClassName("A"))
        builder.generic(reflectType)
        builder.generic(String::class)
        builder.generic(CharSequence::class.java)

        assertThat(getGenerics(builder)).containsExactly("A", "String", "String", "CharSequence").inOrder()
    }

    @ParameterizedTest
    @MethodSource("builders")
    fun `test bounded generic parameter overloads`(builder: GenericMethods<*>) {
        builder.generic("T", ClassName("Comparable"))
        builder.generic("K", String::class)
        builder.generic("V", CharSequence::class.java)

        assertThat(getGenerics(builder)).containsExactly(
            "T extends Comparable",
            "K extends String",
            "V extends CharSequence"
        ).inOrder()
    }

    @ParameterizedTest
    @MethodSource("builders")
    fun `test genericCast and genericCasts overloads`(builder: GenericMethods<*>) {
        builder.genericCast(TypeVariableName("X"))
        builder.genericCasts(TypeVariableName("Y"), TypeVariableName("Z"))

        assertThat(getGenerics(builder)).containsExactly("X", "Y", "Z").inOrder()
    }
}
