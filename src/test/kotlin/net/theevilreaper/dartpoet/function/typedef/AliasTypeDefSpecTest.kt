package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.asTypeName
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals

@DisplayName("Test TypeDefSpec creation")
class AliasTypeDefSpecTest {

    @Test
    fun `test spec definition`() {
        val typeDef = TypeDef.alias("JsonAlias")
            .returns(Map::class.parameterizedBy(String::class.asTypeName(), ClassName("dynamic")))
            .build()
        assertNotNull(typeDef, "The typedef can't be null")
        assertInstanceOf<AliasTypeDefSpec>(typeDef, "The typedef must be an alias type")

        assertNotNull(typeDef.returnType, "The return type must not be null")
        val typeName = typeDef.type

        assertInstanceOf<ClassName>(typeName)
        assertEquals("JsonAlias", typeName.getRawData(), "The raw data must be \"JsonAlias\"")
    }
}

