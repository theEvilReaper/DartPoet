package net.theevilreaper.dartpoet.writer

enum class FragmentPart(
    val part: String,
    val identifier: Char
) {

    NAMED("%N", 'N'),
    LITERAL("%L", 'L'),
    STRING("%S", 'S'),
    STRING_NOT_ESCAPED("%P", 'P'),
    MEMBER("%M", 'M');

    companion object {
        /**
         * Converts a string which contains a part from a fragment into a [FragmentPart] value.
         * @param partString the string which contains a string part
         */
        fun mapToFragmentPart(partString: String): FragmentPart? {
            return values().firstOrNull { it.part === partString }
        }

        fun mapByIdentifier(identifier: Char): FragmentPart? {
            return values().firstOrNull { it.identifier == identifier }
        }
    }
}