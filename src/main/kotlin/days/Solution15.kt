package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

private val String.hash get() = this.fold(0) { acc, c -> 17 * (acc + c.code) % 256 }

private class HashMap {
    val boxes = List(256) { linkedMapOf<String, Int>() }

    fun execute(operation: String) {
        if (operation.endsWith('-')) {
            val label = operation.dropLast(1)
            boxes[label.hash].remove(label)
        } else {
            val pieces = operation.split('=')
            val label = pieces.first()
            val focalLength = pieces.last().toInt()
            boxes[label.hash][label] = focalLength
        }
    }

    val focusingPower
        get() = boxes.withIndex()
            .sumOf { (box, lenses) ->
                lenses.values
                    .withIndex()
                    .sumOf { (slot, focalLength) -> (box + 1) * (slot + 1) * focalLength }
            }
}

object Solution15 : Solution<List<String>>(AOC_YEAR, 15) {
    override fun getInput(handler: InputHandler) = handler.getInput(",")

    override fun solve(input: List<String>): PairOf<Int> {
        val ans1 = input.sumOf(String::hash)
        val hashMap = HashMap()
        input.forEach(hashMap::execute)
        val ans2 = hashMap.focusingPower
        return ans1 to ans2
    }
}
