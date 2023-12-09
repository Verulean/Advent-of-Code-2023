package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints

typealias Layer = List<Int>
typealias Pyramid = PairOf<List<Int>>

object Solution09 : Solution<List<Layer>>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::ints)

    private fun pascalify(base: Layer): Pyramid {
        val pyramid = mutableListOf<Int>() to mutableListOf<Int>()
        var currLayer = base
        while (currLayer.any { it != 0 }) {
            pyramid.first.add(currLayer.first())
            pyramid.second.add(currLayer.last())
            currLayer = currLayer.zip(currLayer.drop(1)).map { it.second - it.first }
        }
        return pyramid
    }

    private val Pyramid.nextValue get() = this.second.sum()

    private val Pyramid.prevValue get() = this.first.foldRight(0, Int::minus)

    override fun solve(input: List<Layer>): PairOf<Int> {
        val pyramids = input.map(::pascalify)
        return pyramids.sumOf { it.nextValue } to pyramids.sumOf { it.prevValue }
    }
}
