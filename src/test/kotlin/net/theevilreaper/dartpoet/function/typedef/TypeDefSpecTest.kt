package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test TypeDefSpec creation")
class TypeDefSpecTest {

    @Disabled
    @Test
    fun `test to builder method`() {
        val typeSpec = TypeDef.alias("Test", Int::class.asTypeName())
            .returns(String::class).build()
        assertNotEquals(Void::class.java, typeSpec.returnType)

        val newBuilder = typeSpec.toBuilder()
    }
}
