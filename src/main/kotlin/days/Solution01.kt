package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution01 : Solution<List<String>>(AOC_YEAR, 1) {
    override fun getInput(handler: InputHandler) = handler.getInput(delimiter = "\n")

    private val numbers = (1..9).associateBy(Int::toString)

    private val words = sequenceOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        .mapIndexed { index, name -> name to index + 1 }
        .toMap()

    private fun parseLine(line: String, calibrationMap: Map<String, Int>): Int {
        fun getDigit(searchFunction: (Collection<String>) -> Pair<Int, String>?) = calibrationMap.getValue(checkNotNull(searchFunction(calibrationMap.keys)).second)
        return getDigit(line::findAnyOf) * 10 + getDigit(line::findLastAnyOf)
    }

    override fun solve(input: List<String>): PairOf<Int> {
        val ans1 = input.sumOf { parseLine(it, numbers) }
        val ans2 = input.sumOf { parseLine(it, numbers + words) }
        return ans1 to ans2
    }
}
