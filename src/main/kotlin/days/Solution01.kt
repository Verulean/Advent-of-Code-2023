package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution01 : Solution<List<String>>(AOC_YEAR, 1) {
    override fun getInput(handler: InputHandler) = handler.getInput(delimiter = "\n")

    private val numbers = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
    )

    private fun parseLine(line: String, allowWords: Boolean): Int {
        var digits = ""
        line.forEachIndexed { i, c ->
            if (c.isDigit()) {
                digits += c
            } else if (allowWords) {
                for ((name, value) in numbers) {
                    if (line.substring(i).startsWith(name)) {
                        digits += value
                        break
                    }
                }
            }
        }
        return if (digits.isNotEmpty()) "${digits.first()}${digits.last()}".toInt() else 0
    }

    override fun solve(input: List<String>): PairOf<Int> {
        val ans1 = input.sumOf { parseLine(it, false) }
        val ans2 = input.sumOf { parseLine(it, true) }
        return ans1 to ans2
    }
}
