package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import kotlin.math.max

typealias TrailMap = Map<Point2D, Map<Point2D, Int>>

private val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
private val slopeToDirection = listOf('v', '>', '^', '<').zip(directions).toMap()

object Solution23 : Solution<Triple<TrailMap, Point2D, Point2D>>(AOC_YEAR, 23) {
    override fun getInput(handler: InputHandler): Triple<TrailMap, Point2D, Point2D> {
        val grid = handler.getInput("\n")
        val digraph = mutableMapOf<Point2D, Map<Point2D, Int>>().withDefault { mapOf() }
        val start = 0 to 1
        val end = grid.size - 1 to grid[0].length - 2
        val seen = mutableSetOf<Point2D>()
        val queue = ArrayDeque<Triple<Int, Point2D, Point2D>>()
        queue.add(Triple(0, start, start))
        while (queue.isNotEmpty()) {
            val (n, curr, junction) = queue.removeLast()
            var paths = 0
            val nextPoints = mutableListOf<Point2D>()
            for (d in directions) {
                val (ii, jj) = curr + d
                if (ii !in grid.indices || jj !in grid[0].indices) continue
                val c = grid[ii][jj]
                if (c == '#') continue
                paths++
                if (ii to jj in seen) continue
                val slopeDirection = slopeToDirection[c]
                if (slopeDirection != null && d != slopeDirection) continue
                nextPoints.add(ii to jj)
            }
            if (paths > 2 || curr == end) {
                digraph[junction] = digraph.getValue(junction) + (curr to n)
                nextPoints.forEach { queue.add(Triple(1, it, curr)) }
            } else {
                seen.add(curr)
                nextPoints.forEach { queue.add(Triple(n + 1, it, junction)) }
            }
        }
        return Triple(digraph, start, end)
    }

    private fun TrailMap.findLongestPath(start: Point2D, end: Point2D): Int {
        var ret = 0
        val queue = ArrayDeque<Triple<Int, Point2D, Set<Point2D>>>()
        queue.add(Triple(0, start, setOf(start)))
        while (queue.isNotEmpty()) {
            val (cost, curr, seen) = queue.removeLast()
            if (curr == end) ret = max(ret, cost)
            else this.getValue(curr).entries
                .filter { it.key !in seen }
                .forEach { (adj, edgeCost) -> queue.add(Triple(cost + edgeCost, adj, seen + adj)) }
        }
        return ret
    }

    private fun TrailMap.toGraph(): TrailMap {
        val graph = mutableMapOf<Point2D, Map<Point2D, Int>>().withDefault { mapOf() }
        this.entries.forEach { (node, adjs) ->
            adjs.entries.forEach { (adj, cost) ->
                graph[node] = graph.getValue(node) + (adj to cost)
                graph[adj] = graph.getValue(adj) + (node to cost)
            }
        }
        return graph
    }

    override fun solve(input: Triple<TrailMap, Point2D, Point2D>): PairOf<Int> {
        val (digraph, start, end) = input
        val ans1 = digraph.findLongestPath(start, end)
        val ans2 = digraph.toGraph().findLongestPath(start, end)
        return ans1 to ans2
    }
}
