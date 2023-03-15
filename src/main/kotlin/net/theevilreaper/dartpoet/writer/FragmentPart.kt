package net.theevilreaper.dartpoet.writer

enum class FragmentPart(
    val part: String
) {

    LITERAL("%L"),
    STRING("%S"),
    STRING_NOT_ESCAPED("%P"),
    TYPE("%T");


    companion object {
        /**
         * Converts a string which contains a part from a fragment into a [FragmentPart] value.
         * @param partString the string which contains a string part
         */
        fun mapToFragmentPart(partString: String): FragmentPart? {
            return values().firstOrNull { it.part === partString }
        }
    }
}