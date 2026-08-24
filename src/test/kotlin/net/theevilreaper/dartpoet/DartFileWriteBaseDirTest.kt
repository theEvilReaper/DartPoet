package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

@DisplayName("Test DartFile#write(path, baseDir) with relative imports across sibling directories")
class DartFileWriteBaseDirTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `write into subdirectory with explicit baseDir allows sibling import`() {
        val libRoot = tempDir.resolve("lib")
        val enchantmentDir = libRoot.resolve("enchantment")
        Files.createDirectories(enchantmentDir)

        val dartFile = DartFile.builder("armor_enchantment")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/enchantment"))
            .build()

        dartFile.write(enchantmentDir, baseDir = libRoot)

        val writtenFile = enchantmentDir.resolve("armor_enchantment.dart")
        assertThat(Files.exists(writtenFile)).isTrue()
        assertThat(Files.readString(writtenFile)).contains("import '../api/enchantment';")
    }

    @Test
    fun `write into subdirectory without baseDir keeps previous strict behaviour`() {
        val libRoot = tempDir.resolve("lib")
        val enchantmentDir = libRoot.resolve("enchantment")
        Files.createDirectories(enchantmentDir)

        val dartFile = DartFile.builder("armor_enchantment")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../api/enchantment"))
            .build()

        // baseDir defaults to the write target itself, matching pre-patch behaviour:
        // this import legitimately escapes `enchantmentDir`, so it must still fail.
        val exception = assertThrows(IllegalArgumentException::class.java) {
            dartFile.write(enchantmentDir)
        }
        assertThat(exception.message).contains("outside project directory")
    }

    @Test
    fun `write rejects a baseDir that is not an ancestor of the write target`() {
        val libRoot = tempDir.resolve("lib")
        val unrelatedDir = tempDir.resolve("other")
        Files.createDirectories(unrelatedDir)
        Files.createDirectories(libRoot)

        val dartFile = DartFile.builder("armor_enchantment")
            .build()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            dartFile.write(unrelatedDir, baseDir = libRoot)
        }
        assertThat(exception.message).contains("must be located inside baseDir")
    }

    @Test
    fun `write still rejects imports escaping the wider baseDir`() {
        val libRoot = tempDir.resolve("lib")
        val enchantmentDir = libRoot.resolve("enchantment")
        Files.createDirectories(enchantmentDir)

        val dartFile = DartFile.builder("armor_enchantment")
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../../outside"))
            .build()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            dartFile.write(enchantmentDir, baseDir = libRoot)
        }
        assertThat(exception.message).contains("outside project directory")
    }
}
