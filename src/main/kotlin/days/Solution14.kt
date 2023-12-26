package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

typealias RockSet = Set<Point2D>
typealias ReflectorMirror = Triple<RockSet, RockSet, PairOf<Int>>

private class RockAndRoll(
    private val roundRocks: RockSet,
    private val cubeRocks: RockSet,
    private val shape: PairOf<Int>
) {
    private val directions = arrayOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
    private val roundToIteration = mutableMapOf((roundRocks to directions.last()) to 0L)
    private val iterationToRound = mutableMapOf(0L to roundRocks)
    private var cycleStart = 0L
    private var cycleLength = 0L

    private fun roll(round: RockSet, direction: Point2D): RockSet {
        var curr = round
        var changed: Boolean
        do {
            changed = false
            val temp = mutableSetOf<Point2D>()
            for (pos in curr) {
                val nextPos = pos + direction
                val (i, j) = nextPos
                when {
                    i !in 0 until shape.first -> temp.add(pos)
                    j !in 0 until shape.second -> temp.add(pos)
                    nextPos in cubeRocks -> temp.add(pos)
                    nextPos in curr -> temp.add(pos)
                    else -> {
                        temp.add(nextPos)
                        changed = true
                    }
                }
            }
            curr = temp
        } while (changed)
        return curr
    }

    fun findCycle() {
        var round = roundRocks
        for (i in 0 until Int.MAX_VALUE) {
            val d = directions[i % 4]
            round = roll(round, d)
            roundToIteration[round to d]?.let { j ->
                cycleStart = j
                cycleLength = i - j + 1
                return
            }
            roundToIteration[round to d] = i + 1L
            iterationToRound[i + 1L] = round
        }
    }

    private fun getCycleIndex(iteration: Long): Long {
        if (iteration < cycleStart + cycleLength) return iteration
        return (iteration - cycleStart) % cycleLength + cycleStart
    }

    fun getTotalLoad(iteration: Long) = iterationToRound.getValue(getCycleIndex(iteration))
        .sumOf { (i, _) -> (shape.first - i) }
}

object Solution14 : Solution<ReflectorMirror>(AOC_YEAR, 14) {
    override fun getInput(handler: InputHandler): ReflectorMirror {
        val lines = handler.getInput("\n")
        val roundRocks = mutableSetOf<Point2D>()
        val cubeRocks = mutableSetOf<Point2D>()
        lines.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                when (c) {
                    'O' -> roundRocks.add(i to j)
                    '#' -> cubeRocks.add(i to j)
                }
            }
        }
        return Triple(roundRocks, cubeRocks, lines.size to lines[0].length)
    }

    override fun solve(input: ReflectorMirror): PairOf<Int> {
        val (round, cube, shape) = input
        val rockAndRoll = RockAndRoll(round, cube, shape)
        rockAndRoll.findCycle()
        return rockAndRoll.getTotalLoad(1) to rockAndRoll.getTotalLoad(4_000_000_000)
    }
}
