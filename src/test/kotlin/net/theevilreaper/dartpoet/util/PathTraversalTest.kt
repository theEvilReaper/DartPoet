package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.directive.DirectiveFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

/**
 * The [PathTraversalTest] should ensure that no one can use such operations to include files,
 * that are not in the same directory or above the root directory
 */
class PathTraversalTest {


    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `test path traversal attack with multiple dot dot sequences fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("src/lib/models/user.dart")

        // Tries to escape: project/src/lib/models/../../../../../etc/passwd
        val maliciousImport = DirectiveFactory.createRelative("../../../../../etc/passwd")
        val imports = listOf(maliciousImport)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }

        assertTrue(exception.message!!.contains("outside project directory"))
    }

    @Test
    fun `test path traversal to system root fails`() {
        val baseDir = tempDir.resolve("myproject")
        val targetFile = baseDir.resolve("lib/main.dart")

        // Tries to access root filesystem
        val maliciousImport = DirectiveFactory.createRelative("../../../../../../../../../../etc/shadow")
        val imports = listOf(maliciousImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal to Windows system directory fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("src/main.dart")

        // Tries to access Windows system files
        val maliciousImport = DirectiveFactory.createRelative("../../../Windows/System32/config/sam")
        val imports = listOf(maliciousImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal with high depth value fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/main.dart")

        // Using depth parameter to create excessive traversal
        val maliciousImport = DirectiveFactory.createRelative("sensitive_file.txt", depth = 50)
        val imports = listOf(maliciousImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test legitimate deep relative import within project passes`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/features/auth/ui/pages/login_page.dart")

        // Goes up to project root, then into another module
        // project/lib/features/auth/ui/pages/../../../../shared/utils/validator.dart
        val legitimateImport = DirectiveFactory.createRelative("../../../../shared/utils/validator.dart")
        val imports = listOf(legitimateImport)

        // Should pass because resolved path is still within project
        assertDoesNotThrow {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal exactly at boundary fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/main.dart")

        // Goes exactly one level above project: project/lib/../../../outside.dart
        val boundaryImport = DirectiveFactory.createRelative("../../outside.dart")
        val imports = listOf(boundaryImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal with normalized path fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/models/user.dart")

        // Even with normalization tricks, should fail
        // lib/models/../.././../../etc/passwd
        val trickyImport = DirectiveFactory.createRelative("../.././../../etc/passwd")
        val imports = listOf(trickyImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test multiple imports with one malicious fails entire validation`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/main.dart")

        val validImport1 = DirectiveFactory.createRelative("models/user.dart")
        val validImport2 = DirectiveFactory.createRelative("../utils/helper.dart")
        val maliciousImport = DirectiveFactory.createRelative("../../../etc/passwd")

        val imports = listOf(validImport1, validImport2, maliciousImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal with unicode encoding fails`() {
        val baseDir = tempDir.resolve("project")
        val targetFile = baseDir.resolve("lib/main.dart")

        // Some systems might interpret unicode differently
        val unicodeImport = DirectiveFactory.createRelative("..%2F..%2Fetc%2Fpasswd")
        val imports = listOf(unicodeImport)

        // Should either fail validation or throw IllegalStateException in catch block
        assertThrows(Exception::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal from deeply nested file to project root passes`() {
        val baseDir = tempDir.resolve("myapp")
        val targetFile = baseDir.resolve("lib/features/auth/presentation/pages/login/widgets/button.dart")

        // Going from deep nesting back to lib root
        val deepImport = DirectiveFactory.createRelative("../../../../../../models/user.dart")
        val imports = listOf(deepImport)

        // Should pass - resolves to myapp/lib/models/user.dart
        assertDoesNotThrow {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test path traversal one level beyond project root fails`() {
        val baseDir = tempDir.resolve("myapp")
        val targetFile = baseDir.resolve("lib/features/auth/pages/button.dart")

        // One too many ../ - escapes project
        val tooDeepImport = DirectiveFactory.createRelative("../../../../../outside.dart")
        val imports = listOf(tooDeepImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test import from impl to api package within lib`() {
        val baseDir = tempDir.resolve("myproject/lib")
        val targetFile = baseDir.resolve("impl/user_impl.dart")

        // Import from impl/ to api/
        val apiImport = DirectiveFactory.createRelative("../api/user_api.dart")
        val imports = listOf(apiImport)

        // Should pass - both are within lib/
        assertDoesNotThrow {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test import from deeply nested impl to api package`() {
        val baseDir = tempDir.resolve("myproject/lib")
        val targetFile = baseDir.resolve("impl/services/database/user_service.dart")

        // Goes up 3 levels, then down to api
        val apiImport = DirectiveFactory.createRelative("../../../api/models/user_model.dart")
        val imports = listOf(apiImport)

        assertDoesNotThrow {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test import trying to escape lib directory fails`() {
        val baseDir = tempDir.resolve("myproject/lib")
        val targetFile = baseDir.resolve("impl/user_impl.dart")

        // Tries to go outside lib: lib/impl/../../outside.dart
        val maliciousImport = DirectiveFactory.createRelative("../../outside.dart")
        val imports = listOf(maliciousImport)

        assertThrows(IllegalArgumentException::class.java) {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }

    @Test
    fun `test sibling package imports within lib`() {
        val baseDir = tempDir.resolve("myproject/lib")
        val targetFile = baseDir.resolve("impl/user_impl.dart")

        val apiImport = DirectiveFactory.createRelative("../api/user_api.dart")
        val utilsImport = DirectiveFactory.createRelative("../utils/helper.dart")
        val modelsImport = DirectiveFactory.createRelative("../models/user.dart")

        val imports = listOf(apiImport, utilsImport, modelsImport)

        // All stay within lib/
        assertDoesNotThrow {
            PathValidation.validateRelativeImports(imports, baseDir, targetFile)
        }
    }
}
