package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Solution06 : Solution<PairOf<List<String>>>(AOC_YEAR, 6) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")
        .map { it.substringAfter(": ") }
        .map { it.split(' ').filter(String::isNotBlank) }
        .let { it[0] to it[1] }

    private fun List<String>.toLong() = this.joinToString("").toLong()

    private fun winCount(time: Long, distance: Long): Long {
        val discriminant = sqrt((time * time - 4 * distance).toDouble())
        val a = (time - discriminant) / 2
        val b = (time + discriminant) / 2
        return ceil(b).toLong() - floor(a).toLong() - 1
    }

    override fun solve(input: PairOf<List<String>>): PairOf<Long> {
        val ans1 = input.first.map(String::toLong)
            .zip(input.second.map(String::toLong))
            .map { winCount(it.first, it.second) }
            .reduce(Long::times)
        val ans2 = winCount(input.first.toLong(), input.second.toLong())
        return ans1 to ans2
    }
}
