package adventOfCode.util

private fun findMatches(input: CharSequence, pattern: String): List<String> {
    return Regex(pattern).findAll(input).map(MatchResult::value).toList()
}

fun <T> CharSequence.ints(allowNegatives: Boolean = true, transform: (List<Int>) -> T): T {
    val pattern = if (allowNegatives) "-?\\d+" else "\\d+"
    return transform(findMatches(this, pattern).map(String::toInt))
}

fun CharSequence.ints(allowNegatives: Boolean = true): List<Int> {
    return this.ints(allowNegatives) { it }
}

fun <T> CharSequence.longs(allowNegatives: Boolean = true, transform: (List<Long>) -> T): T {
    val pattern = if (allowNegatives) "-?\\d+" else "\\d+"
    return transform(findMatches(this, pattern).map(String::toLong))
}

fun CharSequence.longs(allowNegatives: Boolean = true): List<Long> {
    return this.longs(allowNegatives) { it }
}

fun <T> CharSequence.doubles(allowNegatives: Boolean = true, transform: (List<Double>) -> T): T {
    val pattern = if (allowNegatives) "-?\\d+(?:\\.\\d+)?" else "\\d+(?:\\.\\d+)?"
    return transform(findMatches(this, pattern).map(String::toDouble))
}

fun CharSequence.doubles(allowNegatives: Boolean = true): List<Double> {
    return this.doubles(allowNegatives) { it }
}

fun CharSequence.words(): List<String> {
    val pattern = "[A-Za-z]+"
    return findMatches(this, pattern)
}
