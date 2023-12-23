package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.Point2D
import adventOfCode.util.plus

private val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

object Solution21 : Solution<Triple<Set<Point2D>, Point2D, Int>>(AOC_YEAR, 21) {
    override fun getInput(handler: InputHandler): Triple<Set<Point2D>, Point2D, Int> {
        val grid = handler.getInput("\n")
        val n = grid.size
        var start = -1 to -1
        val garden = mutableSetOf<Point2D>()
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, c ->
                when (c) {
                    '.' -> garden.add(i to j)
                    'S' -> {
                        garden.add(i to j)
                        start = i to j
                    }
                }
            }
        }
        return Triple(garden, start, n)
    }

    override fun solve(input: Triple<Set<Point2D>, Point2D, Int>): Pair<Int?, Long> {
        val (garden, start, n) = input
        var currBoundary = setOf(start)
        var prevBoundary = setOf<Point2D>()
        val currDifferences = mutableListOf(0, 0, 0, 0)
        var prevDifferences = listOf(0, 0, 0, 0)
        val counts = mutableListOf(0, 0)
        var ans1: Int? = null
        val steps = 26501365
        for (step in 0..steps) {
            val parity = step % 2
            counts[parity] += currBoundary.size
            if (step == 64) ans1 = counts[parity]
            if ((steps - step) % n == 0) {
                currDifferences[0] = counts[parity]
                currDifferences.indices.forEach { i ->
                    val diff = currDifferences[i] - prevDifferences[i]
                    if (diff == 0) {
                        val x = ((steps - step) / n).toLong()
                        val (a0, a1, a2, _) = currDifferences
                        val ans2 = a2 * x * (x + 1) / 2 + a1 * x + a0
                        return ans1 to ans2
                    } else if (i + 1 in currDifferences.indices) {
                        currDifferences[i + 1] = diff
                    }
                }
                prevDifferences = currDifferences.toList()
            }
            val temp = currBoundary
                .flatMap { p -> directions.map { p + it } }
                .filter { it !in prevBoundary }
                .filter { (i, j) -> i.mod(n) to j.mod(n) in garden }
                .toSet()
            prevBoundary = currBoundary
            currBoundary = temp
        }
        throw Exception("unreachable")
    }
}
