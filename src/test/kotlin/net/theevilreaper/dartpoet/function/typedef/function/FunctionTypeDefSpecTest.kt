package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Test FunctionTypeDefSpec edge cases")
class FunctionTypeDefSpecTest {

    @Test
    fun `test function typedef without parameters emits empty round brackets`() {
        val function = TypeDef.function("VoidCallback").returns(Void::class).build()
        function.verifyDartOutput("typedef VoidCallback = void Function();")
    }

    @Test
    fun `test function typedef with docs and annotations`() {
        val annotation = AnnotationSpec.builder("deprecated").build()
        val typeDef = TypeDef.function("CustomHandler")
            .doc("Handles a custom event.")
            .annotation(annotation)
            .returns(Void::class)
            .parameter(ParameterSpec.positional("event", String::class).build())
            .build() as FunctionTypeDefSpec

        assertEquals(1, typeDef.docs.size)
        assertEquals(1, typeDef.annotations.size)
        assertEquals(setOf(annotation), typeDef.annotations)

        val rebuilt = typeDef.toBuilder().build() as FunctionTypeDefSpec
        assertEquals(typeDef.docs.size, rebuilt.docs.size)
        assertEquals(typeDef.annotations, rebuilt.annotations)
        assertEquals(typeDef.returnType, rebuilt.returnType)
        assertEquals(typeDef.parameters.size, rebuilt.parameters.size)
    }
}
