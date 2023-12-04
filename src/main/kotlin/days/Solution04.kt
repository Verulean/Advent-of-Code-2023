package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution04 : Solution<List<Int>>(AOC_YEAR, 4) {
    override fun getInput(handler: InputHandler): List<Int> {
        return handler.getInput("\n")
            .map { it.substringAfter(": ").split(" | ") }
            .map { halves ->
                halves.map {
                    it.split(' ')
                        .mapNotNull(String::toIntOrNull)
                        .toSet()
                }
            }
            .map { it.reduce(Set<Int>::intersect) }
            .map(Set<Int>::size)
    }

    override fun solve(input: List<Int>): PairOf<Int> {
        val cardCounts = input.indices
            .associateWith { 1 }
            .toMutableMap()
        val ans1 = input.withIndex()
            .filter { it.value > 0 }
            .sumOf { (i, overlaps) ->
                val cardCount = cardCounts.getValue(i)
                (i + 1..i + overlaps)
                    .filter(cardCounts.keys::contains)
                    .forEach { cardCounts.merge(it, cardCount, Int::plus) }
                1.shl(overlaps - 1)
            }
        val ans2 = cardCounts.values.sum()
        return ans1 to ans2
    }
}
