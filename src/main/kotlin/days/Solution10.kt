package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import kotlin.math.abs

typealias Grid = Array<CharArray>

private operator fun Grid.get(point: Point2D) = this[point.first][point.second]
private operator fun Grid.set(point: Point2D, value: Char) {
    this[point.first][point.second] = value
}

object Solution10 : Solution<Grid>(AOC_YEAR, 10) {
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

    private val pipeBends = setOf('L', 'J', '7', 'F')

    private val pipeOpenings = mapOf(
        '|' to setOf(Direction.NORTH, Direction.SOUTH),
        '-' to setOf(Direction.EAST, Direction.WEST),
        'L' to setOf(Direction.NORTH, Direction.EAST),
        'J' to setOf(Direction.NORTH, Direction.WEST),
        '7' to setOf(Direction.SOUTH, Direction.WEST),
        'F' to setOf(Direction.SOUTH, Direction.EAST),
        '.' to setOf()
    )

    private fun findStart(grid: Grid): Point2D {
        grid.forEachIndexed { i, row ->
            val j = row.indexOf('S')
            if (j >= 0) return i to j
        }
        return -1 to -1
    }

    private fun traversePath(grid: Grid, position: Point2D, direction: Direction): Pair<List<Point2D>, Int> {
        val vertices = mutableListOf<Point2D>()
        var boundaryPoints = 0
        var p = position
        var d = direction
        do {
            p += d.vector
            val tile = grid[p]
            d = pipeOpenings.getValue(tile).first { it != -d }
            if (tile in pipeBends) vertices.add(p)
            boundaryPoints++
        } while (p != position)
        return vertices to boundaryPoints
    }

    override fun solve(input: Grid): PairOf<Int> {
        val startPosition = findStart(input)
        val startOpenings = Direction.entries.filter { -it in pipeOpenings.getValue(input[startPosition + it.vector]) }
        input[startPosition] = pipeOpenings.entries.first { (_, openings) -> (startOpenings - openings).isEmpty() }.key
        val (vertices, boundaryPoints) = traversePath(input, startPosition, startOpenings.first())
        // Shoelace Theorem + Pick's Theorem
        val area = abs(vertices.zip(vertices.drop(1) + vertices.take(1)).sumOf { (p1, p2) ->
            p1.first * p2.second - p1.second * p2.first
        }) / 2
        return boundaryPoints / 2 to area - boundaryPoints / 2 + 1
    }
}
