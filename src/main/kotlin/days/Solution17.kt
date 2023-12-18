package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.*
import java.util.*

typealias IntGrid = List<List<Int>>
typealias CrucibleState = Pair<Point2D, Point2D>

object Solution17 : Solution<IntGrid>(AOC_YEAR, 17) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { it.toCharArray().map(Char::digitToInt) }

    private val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    private fun crucibleDijkstra(grid: IntGrid, minDist: Int, maxDist: Int): Int {
        val startState = (0 to 0) to (0 to 0)
        val end = grid.size - 1 to grid[0].size - 1
        val gScores = mutableMapOf(startState to 0)
        val queue = PriorityQueue<Pair<Int, CrucibleState>> { p1, p2 -> p1.first - p2.first }
        queue.add(0 to startState)
        while (queue.isNotEmpty()) {
            val (cost, state) = queue.remove()
            val (pos, prevDir) = state
            if (pos == end) return cost
            directions.forEach directionLoop@{ dir ->
                if (dir == prevDir || -dir == prevDir) return@directionLoop
                var newCost = cost
                (1..maxDist).forEach lengthLoop@{ d ->
                    val (ii, jj) = pos + d * dir
                    if (ii !in grid.indices || jj !in grid[0].indices) return@lengthLoop
                    newCost += grid[ii][jj]
                    if (d < minDist) return@lengthLoop
                    val newState = (ii to jj) to dir
                    val best = gScores[newState]
                    if (best == null || newCost < best) {
                        gScores[newState] = newCost
                        queue.add(newCost to newState)
                    }
                }
            }
        }
        throw Exception("unreachable")
    }

    override fun solve(input: IntGrid): PairOf<Int> {
        return crucibleDijkstra(input, 1, 3) to crucibleDijkstra(input, 4, 10)
    }
}
