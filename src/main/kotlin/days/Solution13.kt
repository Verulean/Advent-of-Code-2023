package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import kotlin.math.min

object Solution13 : Solution<List<CharGrid>>(AOC_YEAR, 13) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n\n")
        .map { it.split('\n').map(String::toCharArray).toTypedArray() }

    private fun mirrorIndices(i: Int, length: Int) = (0 until min(i, length - i)).map { i - it - 1 to i + it }

    private fun CharGrid.getReflectionScore(smudges: Int): Int {
        (1 until this.size).forEach { i ->
            val indices = mirrorIndices(i, this.size)
            val errors = this[0].indices
                .sumOf { j -> indices.filter { this[it.first][j] != this[it.second][j] }.size }
            if (errors == smudges) return 100 * i
        }
        (1 until this[0].size).forEach { j ->
            val indices = mirrorIndices(j, this[0].size)
            val errors = this.sumOf { row -> indices.filter { row[it.first] != row[it.second] }.size }
            if (errors == smudges) return j
        }
        throw Exception("unreachable")
    }

    override fun solve(input: List<CharGrid>): PairOf<Int> {
        val ans1 = input.sumOf { it.getReflectionScore(0) }
        val ans2 = input.sumOf { it.getReflectionScore(1) }
        return ans1 to ans2
    }
}
