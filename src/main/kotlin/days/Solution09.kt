package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints

typealias Layer = List<Int>

object Solution09 : Solution<List<Layer>>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::ints)

    private fun predictValues(base: Layer): PairOf<Int> {
        if (base.all { it == 0 }) return 0 to 0
        val (next, prev) = predictValues(base.zipWithNext().map { it.second - it.first })
        return base.last() + next to base.first() - prev
    }

    override fun solve(input: List<Layer>): PairOf<Int> {
        operator fun PairOf<Int>.plus(other: PairOf<Int>) = this.first + other.first to this.second + other.second
        return input.map(::predictValues).reduce(PairOf<Int>::plus)
    }
}
