package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.TripleOf
import adventOfCode.util.longs
import kotlin.math.max
import kotlin.math.min

typealias PlantNumber = Long
typealias Almanac = List<List<TripleOf<PlantNumber>>>

object Solution05 : Solution<Pair<List<PlantNumber>, Almanac>>(AOC_YEAR, 5) {
    override fun getInput(handler: InputHandler): Pair<List<PlantNumber>, Almanac> {
        val data = handler.getInput("\n\n")
        val seeds = data[0].longs()
        val almanac = data.drop(1)
            .map { it.split('\n') }
            .map { lines ->
                lines.drop(1)
                    .map { line ->
                        val (destStart, sourceStart, length) = line.longs()
                        Triple(sourceStart, sourceStart + length, destStart - sourceStart)
                    }
            }
        return seeds to almanac
    }

    private fun getBestLocation(almanac: Almanac, start: PlantNumber, length: PlantNumber = 1): PlantNumber? {
        var bestLocation: PlantNumber? = null
        val queue = ArrayDeque<Triple<PlantNumber, PlantNumber, Int>>()
        queue.add(Triple(start, start + length, 0))
        while (queue.isNotEmpty()) {
            val (currStart, currStop, i) = queue.removeFirst()
            if (i == almanac.size) {
                bestLocation = listOfNotNull(bestLocation, currStart).min()
                continue
            }
            var requeue = true
            for ((sourceStart, sourceStop, offset) in almanac[i]) {
                val sharedStart = max(currStart, sourceStart)
                val sharedStop = min(currStop, sourceStop)
                if (sharedStart >= sharedStop) continue
                queue.add(Triple(sharedStart + offset, sharedStop + offset, i + 1))
                if (sharedStart > currStart) queue.add(Triple(currStart, sharedStart, i))
                if (sharedStop < currStop) queue.add(Triple(sharedStop, currStop, i))
                requeue = false
            }
            if (requeue) {
                queue.add(Triple(currStart, currStop, i + 1))
            }
        }
        return bestLocation
    }

    override fun solve(input: Pair<List<PlantNumber>, Almanac>): PairOf<PlantNumber> {
        val (seeds, almanac) = input
        val ans1 = seeds.mapNotNull { getBestLocation(almanac, it) }.min()
        val ans2 = seeds.withIndex()
            .groupBy { it.index / 2 }
            .values
            .mapNotNull { getBestLocation(almanac, it[0].value, it[1].value) }
            .min()
        return ans1 to ans2
    }
}
