package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test TypeDefSpec creation")
class TypeDefSpecTest {

    @Disabled
    @Test
    fun `test to builder method`() {
        val typeSpec = TypeDefSpec.alias("Test", Int::class.asTypeName())
            .returns(String::class).build()
        assertNotEquals(Void::class.java, typeSpec.returnType)

        val newBuilder = typeSpec.toBuilder()
    }
}
