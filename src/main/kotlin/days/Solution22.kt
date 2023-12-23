package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.ints
import kotlin.math.max

object Solution22 : Solution<Pair<Int, List<Set<Int>>>>(AOC_YEAR, 22) {
    private operator fun List<Int>.component6() = this[5]

    override fun getInput(handler: InputHandler): Pair<Int, List<Set<Int>>> {
        val coordinates = handler.getInput("\n").map(String::ints).sortedBy { it[2] }
        val tallest = mutableMapOf<Point2D, PairOf<Int>>().withDefault { -1 to 0 }
        val dependencyGraph = mutableListOf<Set<Int>>()
        coordinates.forEachIndexed { i, (x0, y0, z0, x1, y1, z1) ->
            var maxHeight = 0
            val dependencies = mutableSetOf<Int>()
            (x0..x1).forEach { x ->
                (y0..y1).forEach { y ->
                    val (j, z) = tallest.getValue(x to y)
                    if (z > maxHeight) {
                        maxHeight = z
                        dependencies.clear()
                        dependencies.add(j)
                    } else if (z == maxHeight) {
                        dependencies.add(j)
                    }
                }
            }
            dependencyGraph.add(dependencies)
            val z = z1 - max(z0 - maxHeight - 1, 0)
            (x0..x1).forEach { x ->
                (y0..y1).forEach { y ->
                    tallest[x to y] = i to z
                }
            }
        }
        return coordinates.size to dependencyGraph
    }

    override fun solve(input: Pair<Int, List<Set<Int>>>): PairOf<Int> {
        val (n, dependencyGraph) = input
        val ans1 = n - dependencyGraph.filter { it.size == 1 }
            .flatten()
            .filter { it >= 0 }
            .toSet()
            .size
        val ans2 = (0 until n).sumOf { i ->
            val affected = mutableSetOf(i)
            dependencyGraph.withIndex()
                .drop(i + 1)
                .forEach { (j, dependencies) ->
                    if ((dependencies - affected).isEmpty()) affected.add(j)
                }
            affected.size - 1
        }
        return ans1 to ans2
    }
}
