package net.theevilreaper.dartpoet.type

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Test cases for the TypeVariableName implementation")
class TypeVariableNameTest {

    @Test
    fun `test emit without bound`() {
        val typeVariable = TypeVariableName("T")
        assertEquals("T", typeVariable.toString())
    }

    @Test
    fun `test emit never includes the bound`() {
        val typeVariable = TypeVariableName("T", ClassName("Comparable"))
        assertEquals("T", typeVariable.toString())
    }

    @Test
    fun `test renderDeclaration without bound`() {
        val typeVariable = TypeVariableName("T")
        assertEquals("T", TypeVariableName.renderDeclaration(typeVariable))
    }

    @Test
    fun `test renderDeclaration with bound`() {
        val typeVariable = TypeVariableName("T", ClassName("Comparable"))
        assertEquals("T extends Comparable", TypeVariableName.renderDeclaration(typeVariable))
    }

    @Test
    fun `test renderDeclaration with nullable bound does not double the marker`() {
        val typeVariable = TypeVariableName("T", ClassName("Comparable").copy(nullable = true))
        assertEquals("T extends Comparable?", TypeVariableName.renderDeclaration(typeVariable))
    }

    @Test
    fun `test renderDeclaration falls back to bare emit for a non-TypeVariableName`() {
        val plain = ClassName("E")
        assertEquals("E", TypeVariableName.renderDeclaration(plain))
    }

    @Test
    fun `test copy preserves the bound`() {
        val bound = ClassName("Comparable")
        val typeVariable = TypeVariableName("T", bound)
        val copied = typeVariable.copy(nullable = true)
        assertTrue { copied.isNullable }
        assertEquals(bound, copied.bound)
    }

    @Test
    fun `test getRawData returns the bare name`() {
        val typeVariable = TypeVariableName("T", ClassName("Comparable"))
        assertEquals("T", typeVariable.getRawData())
    }

    @Test
    fun `test empty name throws`() {
        val exception = assertThrows<IllegalArgumentException> {
            TypeVariableName("   ")
        }
        assertEquals("The name of a TypeVariableName can't be empty", exception.message)
    }

    @Test
    fun `test two TypeVariableName with the same name but different bounds are equal`() {
        // Intentional: equality is based on emit() output (bare name only, per cachedString),
        // not on the bound. This means a Set<TypeName> collapses same-named type variables
        // regardless of bound, which mirrors Dart's own rule that you can't declare the same
        // type parameter name twice on one declaration.
        val first = TypeVariableName("T", ClassName("Comparable"))
        val second = TypeVariableName("T", ClassName("Object"))
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }
}
