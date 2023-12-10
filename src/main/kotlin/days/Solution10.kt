package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.Point2D
import adventOfCode.util.plus

typealias Grid = Array<CharArray>

private operator fun Grid.get(point: Point2D) = this[point.first][point.second]
private operator fun Grid.set(point: Point2D, value: Char) {
    this[point.first][point.second] = value
}

object Solution10 : Solution<Grid>(AOC_YEAR, 10) {
    private var grid: Grid = arrayOf()

    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::toCharArray).toTypedArray()

    private enum class Direction(val vector: Point2D) {
        NORTH(-1 to 0),
        EAST(0 to 1),
        SOUTH(1 to 0),
        WEST(0 to -1)
    }

    private operator fun Direction.unaryMinus() = mapOf(
        Direction.NORTH to Direction.SOUTH,
        Direction.SOUTH to Direction.NORTH,
        Direction.EAST to Direction.WEST,
        Direction.WEST to Direction.EAST
    ).getValue(this)

    private val pipeOpenings = mapOf(
        '|' to setOf(Direction.NORTH, Direction.SOUTH),
        '-' to setOf(Direction.EAST, Direction.WEST),
        'L' to setOf(Direction.NORTH, Direction.EAST),
        'J' to setOf(Direction.NORTH, Direction.WEST),
        '7' to setOf(Direction.SOUTH, Direction.WEST),
        'F' to setOf(Direction.SOUTH, Direction.EAST),
        '.' to setOf()
    )

    private var Point2D.tile
        get() = grid[this]
        set(value) {
            grid[this] = value
        }

    private val Point2D.isInBounds get() = this.first in grid.indices && this.second in grid[0].indices

    private val Point2D.neighbors
        get() = Direction.entries
            .map { d -> d to this + d.vector }
            .filter { it.second.isInBounds }

    private fun findStart(): Point2D {
        grid.forEachIndexed { i, row ->
            val j = row.indexOf('S')
            if (j >= 0) return i to j
        }
        return -1 to -1
    }

    private fun characterizeStart(pos: Point2D): Char {
        val startOpenings = mutableSetOf<Direction>()
        pos.neighbors
            .forEach { (direction, pos) ->
                if (-direction in pipeOpenings.getValue(pos.tile)) startOpenings.add(direction)
            }
        return pipeOpenings.entries.first { (_, openings) -> (startOpenings - openings).isEmpty() }.key
    }

    private fun Point2D.isConnectedTo(other: Point2D, direction: Direction) =
        direction in pipeOpenings.getValue(tile) && -direction in pipeOpenings.getValue(other.tile)

    override fun solve(input: Grid): Pair<Any?, Any?> {
        grid = input
        // Determine starting pipe
        val startPos = findStart()
        startPos.tile = characterizeStart(startPos)
        // Part 1: Flood-fill loop, answer is half of loop size
        var ans1 = 1
        val loopGrid = Array(grid.size) { Array(grid[0].size) { false } }
        val seen = mutableSetOf<Point2D>()
        val queue = mutableListOf(startPos)
        while (queue.isNotEmpty()) {
            val currPos = queue.removeFirst()
            if (currPos in seen) continue
            seen.add(currPos)
            currPos.neighbors
                .filter { (direction, adjPos) -> adjPos !in seen && currPos.isConnectedTo(adjPos, direction) }
                .map { it.second }
                .forEach { adjPos ->
                    loopGrid[adjPos.first][adjPos.second] = true
                    queue.add(adjPos)
                    ans1++
                }
        }
        ans1 /= 2
        // Part 2:
        var ans2 = 0
        grid.forEachIndexed { i, row ->
            var inside = false
            row.forEachIndexed { j, tile ->
                if (tile in setOf('|', 'L', 'J')) inside = !inside
                else if (inside && tile == '.') ans2++
            }
        }
        return ans1 to ans2
    }
}
