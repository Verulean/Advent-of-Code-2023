package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import adventOfCode.util.unaryMinus
import java.util.*

typealias IntGrid = List<List<Int>>
typealias CrucibleState = Triple<Point2D, Point2D, Int>

object Solution17 : Solution<IntGrid>(AOC_YEAR, 17) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { it.toCharArray().map(Char::digitToInt) }

    private val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    private fun crucibleDijkstra(grid: IntGrid, minDist: Int, maxDist: Int): Int {
        val startState = Triple(0 to 0, 0 to 0, 0)
        val end = grid.size - 1 to grid[0].size - 1
        val gScores = mutableMapOf(startState to 0)
        val queue = PriorityQueue<Pair<Int, CrucibleState>> { p1, p2 -> p1.first - p2.first }
        queue.add(0 to startState)
        while (queue.isNotEmpty()) {
            val (cost, state) = queue.remove()
            val (pos, prevDir, length) = state
            if (pos == end && length in minDist..maxDist) return cost
            directions.forEach directionLoop@{ dir ->
                val repeat = dir == prevDir
                when {
                    !repeat && length in 1 until minDist -> return@directionLoop
                    repeat && length >= maxDist -> return@directionLoop
                    -dir == prevDir -> return@directionLoop
                }
                val (ii, jj) = pos + dir
                if (ii !in grid.indices || jj !in grid[0].indices) return@directionLoop
                val newCost = cost + grid[ii][jj]
                val newState = Triple(ii to jj, dir, if (repeat) length + 1 else 1)
                val best = gScores[newState]
                if (best == null || newCost < best) {
                    gScores[newState] = newCost
                    queue.add(newCost to newState)
                }
            }
        }
        throw Exception("unreachable")
    }

    override fun solve(input: IntGrid): PairOf<Int> {
        return crucibleDijkstra(input, 1, 3) to crucibleDijkstra(input, 4, 10)
    }
}
