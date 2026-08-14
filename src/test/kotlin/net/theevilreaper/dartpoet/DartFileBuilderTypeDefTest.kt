package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.function.typedef.function.FunctionTypeDefSpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * [DartFileBuilder.typeDef] is currently typed to accept only [net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefSpec].
 * A [FunctionTypeDefSpec] built via `TypeDef.function(...).build()` is typed as `AbstractTypeDef<*>` and can't be
 * passed to [DartFileBuilder.typeDef] at all - it doesn't compile, since [FunctionTypeDefSpec] isn't a subtype of
 * `AliasTypeDefSpec`. Since that failure can't be expressed as a runtime assertion, this test instead checks
 * reflectively whether a [FunctionTypeDefSpec] could be assigned to the declared parameter type of the
 * `typeDef(...)` overload, so it fails today and starts passing once the parameter type is widened to
 * `AbstractTypeDef<*>` (or another common supertype of both typedef kinds).
 */
@DisplayName("Test that DartFileBuilder.typeDef accepts any AbstractTypeDef")
class DartFileBuilderTypeDefTest {

    @Test
    fun `test typeDef overload accepts a FunctionTypeDefSpec instead of only AliasTypeDefSpec`() {
        val singleArgOverload = DartFileBuilder::class.java.methods
            .filter { it.name == "typeDef" && !it.isVarArgs }
            .single { it.parameterCount == 1 }

        assertThat(FunctionTypeDefSpec::class.java)
            .isAssignableTo(singleArgOverload.parameterTypes[0])
    }
}
