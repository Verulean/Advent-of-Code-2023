package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution11 : Solution<List<CharArray>>(AOC_YEAR, 11) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::toCharArray)

    private fun getDistanceSum(
        galaxies: List<PairOf<Int>>,
        blankXs: Set<Int>,
        blankYs: Set<Int>,
        expansionFactors: List<Long>
    ): List<Long> {
        val ret = List(expansionFactors.size) { 0L }.toMutableList()
        galaxies.asSequence()
            .forEachIndexed { i, start ->
                val (x1, y1) = start
                galaxies.asSequence().drop(i + 1)
                    .forEach { (x2, y2) ->
                        val (sx1, sx2) = if (x1 < x2) x1 to x2 else x2 to x1
                        val (sy1, sy2) = if (y1 < y2) y1 to y2 else y2 to y1
                        val manhattan = sx2 - sx1 + sy2 - sy1
                        val crosses =
                            blankXs.filter { it in sx1..sx2 }.size + blankYs.filter { it in sy1..sy2 }.size
                        expansionFactors.forEachIndexed { j, expansion -> ret[j] += manhattan + (expansion - 1) * crosses }
                    }
            }
        return ret
    }

    override fun solve(input: List<CharArray>): PairOf<Long> {
        val galaxies = input.withIndex()
            .flatMap { (i, row) ->
                row.withIndex()
                    .filter { (_, c) -> c == '#' }
                    .map { (j, _) -> i to j }
            }
        val xs = input.indices.filter { i -> !input[i].contains('#') }.toSet()
        val ys = input[0].indices.filter { j -> input.all { row -> row[j] != '#' } }.toSet()
        val (ans1, ans2) = getDistanceSum(galaxies, xs, ys, listOf(2, 1000000))
        return ans1 to ans2
    }
}
