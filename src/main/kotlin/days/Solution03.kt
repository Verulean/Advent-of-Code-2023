package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D

object Solution03 : Solution<List<String>>(AOC_YEAR, 3) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private fun Char.isSymbol() = !this.isDigit() && this != '.'

    private fun Char.isGear() = this == '*'

    private fun MutableSet<Point2D>.pop(): Point2D {
        val ret = this.first()
        this.remove(ret)
        return ret
    }

    override fun solve(input: List<String>): PairOf<Int> {
        val rowIndices = input.indices
        val colIndices = input[0].indices

        fun getNumbers(row: Int, col: Int): Map<Point2D, Int> {
            val numbers = mutableMapOf<Point2D, Int>()
            val candidates = (row - 1..row + 1)
                .flatMap { i -> (col - 1..col + 1).map { j -> i to j } }
                .filter { (i, j) -> i in rowIndices && j in colIndices }
                .filter { (i, j) -> input[i][j].isDigit() }
                .toMutableSet()
            while (candidates.isNotEmpty()) {
                val (i, j) = candidates.pop()
                val line = input[i]
                val jMin = (j - 1 downTo colIndices.first).find { !line[it].isDigit() } ?: (colIndices.first - 1)
                val jMax = (j + 1..colIndices.last).find { !line[it].isDigit() } ?: (colIndices.last + 1)
                candidates.removeIf { it.first == i && it.second in jMin..jMax }
                numbers[i to jMin] = line.substring(jMin + 1, jMax).toInt()
            }
            return numbers
        }

        val numbers = mutableMapOf<Point2D, Int>()
        var gearRatioSum = 0
        for ((row, line) in input.withIndex()) {
            for ((col, c) in line.withIndex()) {
                if (!c.isSymbol()) continue
                val newNumbers = getNumbers(row, col)
                numbers += newNumbers
                if (c.isGear() && newNumbers.size == 2) {
                    gearRatioSum += newNumbers.values.reduce(Int::times)
                }
            }
        }

        return numbers.values.sum() to gearRatioSum
    }
}
