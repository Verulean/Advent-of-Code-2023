package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution16 : Solution<CharGrid>(AOC_YEAR, 16) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::toCharArray).toTypedArray()

    private fun CharGrid.countEnergizedTiles(position: Point2D, direction: Point2D): Int {
        val seen = mutableSetOf<PairOf<Point2D>>()
        val queue = ArrayDeque<PairOf<Point2D>>()
        queue.add(position to direction)
        do {
            var (p, d) = queue.removeFirst()
            p += d
            if (p to d in seen) continue
            val (i, j) = p
            val (di, dj) = d
            if (i !in this.indices || j !in this.indices) continue
            seen.add(p to d)
            when (this[i][j]) {
                '.' -> queue.add(p to d)
                '/' -> queue.add(p to (-dj to -di))
                '\\' -> queue.add(p to (dj to di))

                '|' -> {
                    if (dj != 0) {
                        queue.add(p to (1 to 0))
                        queue.add(p to (-1 to 0))
                    } else queue.add(p to d)
                }

                '-' -> {
                    if (di != 0) {
                        queue.add(p to (0 to 1))
                        queue.add(p to (0 to -1))
                    } else queue.add(p to d)
                }
            }
        } while (queue.isNotEmpty())
        return seen.map { it.first }.toSet().size
    }

    override fun solve(input: CharGrid): PairOf<Int> {
        val n = input.size
        val ans1 = input.countEnergizedTiles(0 to -1, 0 to 1)
        val ans2 = input.indices.asSequence()
            .flatMap { i ->
                sequenceOf(
                    (i to -1) to (0 to 1),
                    (i to n) to (0 to -1),
                    (-1 to i) to (1 to 0),
                    (n to i) to (-1 to 0)
                )
            }
            .maxOf { input.countEnergizedTiles(it.first, it.second) }
        return ans1 to ans2
    }
}
