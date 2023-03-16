package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.Test

class DartPropertySpecTest {

    @Test
    fun `test logic`() {
        val property = DartPropertySpec.builder("test", "String").nullable(true).build()
        assertThat(property.toString()).isEqualTo("String? test;")
    }
}