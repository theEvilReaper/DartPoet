package net.theevilreaper.dartpoet.enumeration

import net.theevilreaper.dartpoet.clazz.ClassSpec
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled("Test the generation of an enum which has a generic context")
class GenericEnumTest {

    private val genericEnum = "TestEnum"

    @Test
    fun testGenericEnumGeneration() {
        //TODO: Implement the test
        val genericEnum: ClassSpec = ClassSpec.builder(genericEnum)
            .build()
    }
}