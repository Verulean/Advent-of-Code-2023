package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import adventOfCode.util.times
import kotlin.math.abs

typealias LagoonInstruction = PairOf<Pair<Point2D, Int>>

object Solution18 : Solution<List<LagoonInstruction>>(AOC_YEAR, 18) {
    private val directionMap = mapOf(
        "R" to (0 to 1),
        "D" to (1 to 0),
        "L" to (0 to -1),
        "U" to (-1 to 0),
    )

    private val hexDirectionMap = mapOf(
        '0' to (0 to 1),
        '1' to (1 to 0),
        '2' to (0 to -1),
        '3' to (-1 to 0),
    )

    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { line ->
        val (d, l, h) = line.split(' ')
        val pair1 = directionMap.getValue(d) to l.toInt()
        val hex = h.trim('(', '#', ')')
        val pair2 = hexDirectionMap.getValue(hex.last()) to hex.take(hex.length - 1).toInt(16)
        pair1 to pair2
    }

    private val Iterable<Point2D>.shoelaceArea
        get() = abs(this.zip(this.drop(1) + this.take(1)).sumOf { (p1, p2) ->
            p1.first.toLong() * p2.second - p1.second.toLong() * p2.first
        }) / 2

    private fun getLagoonVolume(instructions: Iterable<Pair<Point2D, Int>>): Long {
        val vertices =
            listOf(0 to 0) + instructions.runningFold(0 to 0) { pos, (direction, distance) -> pos + direction * distance }
        val pathLength = instructions.sumOf { it.second }
        return vertices.shoelaceArea + pathLength / 2 + 1
    }

    override fun solve(input: List<LagoonInstruction>): PairOf<Long> {
        val ans1 = getLagoonVolume(input.map { it.first })
        val ans2 = getLagoonVolume(input.map { it.second })
        return ans1 to ans2
    }
}
