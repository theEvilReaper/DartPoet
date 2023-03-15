package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DartPropertyTest {

    @Test
    fun `test logic`() {
        val property = DartProperty.builder("test", "String").nullable(true).build()
        assertThat(property.toString()).isEqualTo("String? test;")
    }
}