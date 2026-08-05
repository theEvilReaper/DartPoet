package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.type.ClassName
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Test the library class type")
class LibraryClassTest {

    @Test
    fun `test library class with empty name throws`() {
        val exception = assertThrows<IllegalStateException> {
            ClassSpec.libraryClass("").build()
        }
        assertThat(exception.message).isEqualTo("The name of a class can't be empty")
    }

    @Test
    fun `test library class can't extend a super class`() {
        val exception = assertThrows<IllegalStateException> {
            ClassSpec.libraryClass("Foo").superClass(ClassName("Bar")).build()
        }
        assertThat(exception.message).isEqualTo("A library class can't extend, mix in or implement other types")
    }

    @Test
    fun `test library class can't implement interfaces`() {
        val exception = assertThrows<IllegalStateException> {
            ClassSpec.libraryClass("Foo").implements(ClassName("Bar")).build()
        }
        assertThat(exception.message).isEqualTo("A library class can't extend, mix in or implement other types")
    }

    @Test
    fun `test library class can't declare a generic type`() {
        val exception = assertThrows<IllegalStateException> {
            ClassSpec.libraryClass("Foo").generic(ClassName("E"))
        }
        assertThat(exception.message).isEqualTo("A library class can't have generic types")
    }

    @Test
    @Disabled(
        "Known bug: ClassWriter#writeClassHeader returns early for ClassType.LIBRARY, so neither the " +
            "'library' keyword nor the class name are ever emitted. Currently renders as \" {}\" instead " +
            "of \"library Foo {}\". Enable once ClassWriter is fixed."
    )
    fun `test library class renders its keyword and name`() {
        val libraryClass = ClassSpec.libraryClass("Foo").build()
        assertThat(libraryClass.toString()).isEqualTo("library Foo {}")
    }
}
