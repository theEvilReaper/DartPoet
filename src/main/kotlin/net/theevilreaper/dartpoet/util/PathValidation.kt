package net.theevilreaper.dartpoet.util

internal object PathValidation {

    private val unallowedNames: List<String> = listOf(
        "..",
        "/",
        "\\"
    )

    fun validateFileName(fileName: String){
        require(!fileName.contains("\u0000")) {
            "Filename contains null byte characters"
        }

        require(!unallowedNames.contains(fileName)) {
            "Filename '$fileName' contains invalid path traversal characters"
        }
    }
}