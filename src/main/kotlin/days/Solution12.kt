package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.TripleOf
import adventOfCode.util.ints

typealias Hotspring = Pair<String, List<Int>>

object Solution12 : Solution<List<Hotspring>>(AOC_YEAR, 12) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")
        .map { it.split(' ') }
        .map { it.first() to it.last().ints() }

    private fun getArrangements(hotspring: Hotspring): Long {
        val (line, groupLengths) = hotspring
        val dp = mutableMapOf<TripleOf<Int>, Long>()
        fun f(i: Int, j: Int, l: Int): Long {
            val k = Triple(i, j, l)
            if (k in dp) return dp.getValue(k)
            if (i == line.length) {
                return when {
                    j == groupLengths.size && l == 0 -> 1
                    j == groupLengths.size - 1 && l == groupLengths.last() -> 1
                    else -> 0
                }
            }
            var ret = 0L
            when (line[i]) {
                '.' -> {
                    if (l == 0) ret += f(i + 1, j, 0)
                    if (j < groupLengths.size && l == groupLengths[j]) ret += f(i + 1, j + 1, 0)
                }

                '#' -> ret += f(i + 1, j, l + 1)
                else -> {
                    if (l == 0) ret += f(i + 1, j, 0)
                    if (j < groupLengths.size && l == groupLengths[j]) ret += f(i + 1, j + 1, 0)
                    ret += f(i + 1, j, l + 1)
                }
            }
            dp[k] = ret
            return ret
        }
        return f(0, 0, 0)
    }

    override fun solve(input: List<Hotspring>): PairOf<Long> {
        val ans1 = input.sumOf(::getArrangements)
        val ans2 =
            input.map { (line, groupLengths) -> List(5) { line }.joinToString("?") to (1..5).flatMap { groupLengths } }
                .sumOf(::getArrangements)
        return ans1 to ans2
    }
}
