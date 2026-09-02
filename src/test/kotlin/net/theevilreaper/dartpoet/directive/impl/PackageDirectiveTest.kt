package net.theevilreaper.dartpoet.directive.impl

import net.theevilreaper.dartpoet.directive.CastType
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PackageDirectiveTest {

    companion object {
        @JvmStatic
        fun invalidCastCombinations() = listOf(
            Arguments.of(CastType.AS, null),
            Arguments.of(null, "test")
        )
    }

    @ParameterizedTest(name = "Test invalid combinations for castType: {0}, importCast: {1}")
    @MethodSource("invalidCastCombinations")
    fun `test invalid cast arguments`(
        castType: CastType?,
        importCast: String?
    ) {
        val exception = assertThrows(IllegalStateException::class.java) {
            DirectiveFactory.create(
                directive = DirectiveType.PACKAGE,
                path = "my_local_file",
                castType = castType,
                importCast = importCast
            )
        }

        assertEquals(
            "The castType and importCast must be set together or must be null. A mixed state is not allowed",
            exception.message
        )
    }

    @Test
    fun `test package directive creation with a given dart suffix`() {
        val path  = "my_local_file.dart"
        val packageDirective = DirectiveFactory.createPackage(path)

        assertInstanceOf(PackageDirective::class.java, packageDirective)
        assertEquals(path, packageDirective.getRawPath())

        val createdDirectiveAsString = packageDirective.asString()
        assertNotNull(createdDirectiveAsString)
        assertEquals("import '$path';", createdDirectiveAsString)
    }

    @Test
    fun `test package directive creation`() {
        val path  = "my_local_file"
        val packageDirective = DirectiveFactory.createPackage(path)

        assertInstanceOf(PackageDirective::class.java, packageDirective)
        assertEquals(path, packageDirective.getRawPath())

        val createdDirectiveAsString = packageDirective.asString()
        assertNotNull(createdDirectiveAsString)
        assertEquals("import '$path.dart';", createdDirectiveAsString)
    }
}
