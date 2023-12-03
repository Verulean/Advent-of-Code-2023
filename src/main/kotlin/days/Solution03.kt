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

        fun getNumbers(i: Int, j: Int): Map<Point2D, Int> {
            val candidates = (i - 1..i + 1).filter(rowIndices::contains)
                .flatMap { ii ->
                    (j - 1..j + 1).filter(colIndices::contains)
                        .map { jj -> ii to jj }
                }
                .toMutableSet()
            val numbers = mutableMapOf<Point2D, Int>()
            while (candidates.isNotEmpty()) {
                val (ii, jj) = candidates.pop()
                val row = input[ii]
                if (!row[jj].isDigit()) continue
                var jMin = jj
                var jMax = jj
                while (jMin > colIndices.first && row[jMin - 1].isDigit()) {
                    jMin--
                    candidates.remove(ii to jMin)
                }
                while (jMax < colIndices.last && row[jMax + 1].isDigit()) {
                    jMax++
                    candidates.remove(ii to jMax)
                }
                val number = row.substring(jMin, jMax + 1).toInt()
                numbers[ii to jMin] = number
            }
            return numbers
        }

        val numbers = mutableMapOf<Point2D, Int>()
        var gearRatioSum = 0
        for ((i, row) in input.withIndex()) {
            for ((j, c) in row.withIndex()) {
                if (!c.isSymbol()) continue
                val new_numbers = getNumbers(i, j)
                numbers += new_numbers
                if (c.isGear() && new_numbers.size == 2) {
                    gearRatioSum += new_numbers.values.reduce(Int::times)
                }
            }
        }

        return numbers.values.sum() to gearRatioSum
    }
}
