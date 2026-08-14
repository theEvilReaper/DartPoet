package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.function.typedef.TypeDef
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test FunctionTypeDefSpec edge cases")
class FunctionTypeDefSpecTest {

    @Test
    fun `test function typedef without parameters emits empty round brackets`() {
        val function = TypeDef.function("VoidCallback").returns(Void::class).build()
        function.verifyDartOutput("typedef VoidCallback = void Function();")
    }
}
