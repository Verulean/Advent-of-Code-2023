package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints

typealias Layer = List<Int>
typealias Sides = PairOf<Int>
typealias Pyramid = List<Sides>

object Solution09 : Solution<List<Layer>>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::ints)

    private fun pascalify(base: Layer): Pyramid {
        val pyramid = mutableListOf<PairOf<Int>>()
        var currLayer = base
        while (currLayer.any { it != 0 }) {
            pyramid.add(currLayer.first() to currLayer.last())
            currLayer = currLayer.zip(currLayer.drop(1)).map { it.second - it.first }
        }
        return pyramid
    }

    private val Pyramid.nextValue get() = this.sumOf(Sides::second)

    private val Pyramid.prevValue get() = this.map(Sides::first).foldRight(0, Int::minus)

    override fun solve(input: List<Layer>): PairOf<Int> {
        val pyramids = input.map(::pascalify)
        return pyramids.sumOf { it.nextValue } to pyramids.sumOf { it.prevValue }
    }
}
