package net.theevilreaper.dartpoet.classTypes

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.type.ClassName
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Test the library class type")
class LibraryClassTest {

    private lateinit var builder: ClassBuilder

    @BeforeEach
    fun setUp() {
        builder = ClassSpec.libraryClass("Foo")
    }

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
            builder.superClass(ClassName("Bar")).build()
        }
        assertThat(exception.message).isEqualTo("A library class can't extend, mix in or implement other types")
    }

    @Test
    fun `test library class can't implement interfaces`() {
        val exception = assertThrows<IllegalStateException> {
            builder.implements(ClassName("Bar")).build()
        }
        assertThat(exception.message).isEqualTo("A library class can't extend, mix in or implement other types")
    }

    @Test
    fun `test library class can't declare a generic type`() {
        val exception = assertThrows<IllegalStateException> {
            builder.generic(ClassName("E"))
        }
        assertThat(exception.message).isEqualTo("A library class can't have generic types")
    }

    @Test
    fun `test library class can't declare properties`() {
        val exception = assertThrows<IllegalStateException> {
            builder.property(PropertySpec.builder("id", Int::class).build())
        }
        assertThat(exception.message).isEqualTo("A library class can't declare functions, properties, constructors or constants")
    }

    @Test
    fun `test library class can't declare functions`() {
        val exception = assertThrows<IllegalStateException> {
            builder.function(FunctionSpec.builder("test").build())
        }
        assertThat(exception.message).isEqualTo("A library class can't declare functions, properties, constructors or constants")
    }

    @Test
    fun `test library class can't declare constructors`() {
        val exception = assertThrows<IllegalStateException> {
            builder.constructor(ConstructorSpec.builder("Foo").build())
        }
        assertThat(exception.message).isEqualTo("A library class can't declare functions, properties, constructors or constants")
    }

    @Test
    fun `test library class can't declare constants`() {
        val exception = assertThrows<IllegalStateException> {
            builder.constant(ConstantPropertySpec.classConst("id", Int::class).initWith("%L", 1).build())
        }
        assertThat(exception.message).isEqualTo("A library class can't declare functions, properties, constructors or constants")
    }
}
