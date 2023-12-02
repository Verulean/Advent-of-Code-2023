package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import kotlin.math.max

object Solution02 : Solution<List<String>>(AOC_YEAR, 2) {
    enum class Color {
        RED, GREEN, BLUE
    }

    private val colorMap = mapOf("red" to Color.RED, "green" to Color.GREEN, "blue" to Color.BLUE)

    override fun getInput(handler: InputHandler) = handler.getInput(delimiter = "\n")

    private fun minimumCubes(game: String): Map<Color, Int> {
        val minCubes = Color.entries
            .associateWith { 0 }
            .toMutableMap()
        game.substringAfter(':')
            .split(';', ',')
            .map { it.trim().split(' ') }
            .forEach { minCubes.merge(colorMap.getValue(it.last()), it.first().toInt(), ::max) }
        return minCubes
    }

    override fun solve(input: List<String>): PairOf<Int> {
        var ans1 = 0
        var ans2 = 0
        input.map(::minimumCubes)
            .forEachIndexed { index, minCubes ->
                val (red, green, blue) = Color.entries.map(minCubes::getValue)
                if (red <= 12 && green <= 13 && blue <= 14) ans1 += index + 1
                ans2 += red * green * blue
            }
        return ans1 to ans2
    }
}
