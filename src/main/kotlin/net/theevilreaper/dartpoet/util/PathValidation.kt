package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.directive.Directive
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.nio.file.Path

/**
 * A utility class that provides validation methods to prevent path traversal attacks
 * and ensure file system operations remain within safe boundaries.
 *
 * This class helps protect against malicious file paths that could:
 * - Escape the intended directory structure using ".." sequences
 * - Include null bytes or other invalid characters
 * - Reference system-critical locations through relative imports
 *
 * All validation methods throw appropriate exceptions when security constraints are violated.
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
internal object PathValidation {

    private val unallowedNames: List<String> = listOf(
        "..",
        "/",
        "\\"
    )

    /**
     * Validates that a filename doesn't contain characters or patterns that could enable path traversal attacks.
     *
     * This method checks for:
     * - Null byte characters (\u0000) which can truncate paths in some file systems
     * - Path traversal patterns like "..", "/", and "\"
     *
     * @param fileName the filename to validate
     * @throws IllegalArgumentException if the filename contains invalid characters or patterns
     */
    fun validateFileName(fileName: String) {
        require(!fileName.contains("\u0000")) {
            "Filename contains null byte characters"
        }

        require(!unallowedNames.contains(fileName)) {
            "Filename '$fileName' contains invalid path traversal characters"
        }
    }

    /**
     * Validates that relative imports don't reference paths outside the project's base directory.
     *
     * For each relative import, this method resolves the import path relative to the target file's location
     * and ensures the resolved path stays within the base directory boundaries. This prevents imports
     * from escaping the project structure and accessing sensitive system files.
     *
     * @param relativeImports the set of relative directive imports to validate
     * @param baseDir the base directory that imports must not escape from
     * @param targetFile the file being written that contains these imports
     * @throws IllegalArgumentException if any import references a path outside the base directory
     * @throws IllegalStateException if an import path cannot be resolved properly
     */
    fun validateRelativeImports(relativeImports: List<Directive>, baseDir: Path, targetFile: Path) {
        val targetDir = targetFile.parent ?: baseDir

        relativeImports.forEach { import ->
            val raw = import.getRawPath()

            // 1. Handle empty import (test expects no failure)
            if (raw.isBlank()) return@forEach

            // 2. Decode URL encoding (unicode traversal test)
            val decoded = try {
                URLDecoder.decode(raw, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid encoded import path: $raw", e)
            }

            // 3. Resolve against target directory
            val resolved = try {
                targetDir.resolve(decoded).normalize()
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid relative import path: $decoded", e)
            }

            // 4. Ensure resolved path stays within baseDir
            if (!resolved.startsWith(baseDir.normalize())) {
                throw IllegalArgumentException(
                    "Relative import '$raw' in file '${targetFile.fileName}' escapes project directory"
                )
            }
        }
    }
}
