package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D

object Solution03 : Solution<List<CharArray>>(AOC_YEAR, 3) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n", true, String::toCharArray)

    private fun Char.isSymbol() = !this.isDigit() && this != '.'

    private fun Char.isGear() = this == '*'

    override fun solve(input: List<CharArray>): PairOf<Int> {
        val m = input.size
        val n = input[0].size
        val numberBuilder = StringBuilder(n)
        val gearMap = mutableMapOf<Point2D, MutableList<Int>>()

        var ans1 = 0

        fun checkNumber(i: Int, j: Int) {
            val l = numberBuilder.length
            if (l == 0) return
            val number = numberBuilder.toString().toInt()
            val isValid = (i - 1..i + 1).intersect(0..<m).any { ii ->
                val js = if (ii != i) j - l - 1..j else listOf(j - l - 1, j)
                js.intersect(0..<n).any { jj ->
                    val cc = input[ii][jj]
                    if (cc.isGear()) {
                        if (ii to jj in gearMap) gearMap.getValue(ii to jj).add(number)
                        else gearMap[ii to jj] = mutableListOf(number)
                    }
                    cc.isSymbol()
                }
            }
            if (isValid) ans1 += number
            numberBuilder.clear()
        }

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                if (c.isDigit()) {
                    numberBuilder.append(c)
                } else {
                    checkNumber(i, j)
                }
            }
            checkNumber(i, n)
        }

        val ans2 = gearMap.values
            .filter { it.size == 2 }
            .sumOf { it.reduce(Int::times) }

        return ans1 to ans2
    }
}
