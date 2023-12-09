package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.ints

typealias Layer = List<Int>
typealias Pyramid = List<Layer>

object Solution09 : Solution<List<Layer>>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map(String::ints)

    private fun pascalify(base: Layer): Pyramid {
        val pyramid = mutableListOf<Layer>()
        var currLayer = base
        while (currLayer.any { it != 0 }) {
            pyramid.add(currLayer)
            currLayer = currLayer.zip(currLayer.drop(1)).map { it.second - it.first }
        }
        return pyramid
    }

    private val Pyramid.nextValue get() = this.sumOf { it.last() }

    private val Pyramid.prevValue get() = this.map { it.first() }.foldRight(0, Int::minus)

    override fun solve(input: List<Layer>): Pair<Any?, Any?> {
        val pyramids = input.map(::pascalify)
        return pyramids.sumOf { it.nextValue } to pyramids.sumOf { it.prevValue }
    }
}
