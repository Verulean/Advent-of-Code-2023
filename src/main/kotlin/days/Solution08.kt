package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.TripleOf
import kotlin.math.sqrt

typealias TurnOrder = CharArray
typealias NodeMap = Map<String, PairOf<String>>
typealias CycleNumber = Long

object Solution08 : Solution<Pair<TurnOrder, NodeMap>>(AOC_YEAR, 8) {
    private val Long.primeFactors
        get(): Map<Long, Int> {
            fun MutableMap<Long, Int>.add(n: Long) = this.merge(n, 1, Int::plus)
            val factors = mutableMapOf<Long, Int>()
            var n = this
            while (n % 2 == 0L) {
                factors.add(2)
                n /= 2
            }
            val squareRoot = sqrt(n.toDouble()).toLong()
            for (d in 3..squareRoot step 2) {
                while (n % d == 0L) {
                    factors.add(d)
                    n /= d
                }
            }
            if (n > 2) {
                factors.add(n)
            }
            return factors
        }

    private infix fun Long.`^`(n: Int) = (1..n).map { n }.fold(1, Long::times)

    private fun Long.modularInverse(m: Long): Long {
        val modThis = this % m
        return (1..<modThis).firstOrNull { (modThis * it) % m == 1L } ?: 0
    }

    private fun gcd(x: Long, y: Long): Long {
        var a = x
        var b = y
        while (b > 0) {
            val temp = b
            b = a % b
            a = temp
        }
        return a
    }

    private fun lcm(x: Long, y: Long): Long {
        return x * (y / gcd(x, y))
    }

    private fun chineseRemainderTheorem(congruences: List<PairOf<CycleNumber>>): CycleNumber? {
        // Decompose moduli into prime powers
        val expandedCongruences = mutableMapOf<CycleNumber, List<Pair<Int, CycleNumber>>>()
        congruences.forEach { (m, r) ->
            m.primeFactors.forEach { (p, power) ->
                expandedCongruences.merge(p, listOf(power to (r `^` power)), List<Pair<Int, CycleNumber>>::plus)
            }
        }
        // Check for any conflicting congruences
        val baseCongruences = mutableMapOf<CycleNumber, Set<CycleNumber>>()
        val maxPrimePower = mutableMapOf<CycleNumber, Int>().withDefault { 0 }
        val maxPrimeRemainder = mutableMapOf<CycleNumber, CycleNumber>()
        expandedCongruences.forEach { (p, candidates) ->
            candidates.forEach { (power, r) ->
                baseCongruences.merge(p, setOf(r % p), Set<Long>::union)
                if (power > maxPrimePower.getValue(p)) {
                    maxPrimePower[p] = power
                    maxPrimeRemainder[p] = r
                }
            }
        }
        if (baseCongruences.values.any { it.size > 1 }) return null
        // Apply coprime case of CRT
        val coprimeCongruences = maxPrimePower.entries.associate { (p, power) -> p `^` power to maxPrimeRemainder.getValue(p) }
        val product = coprimeCongruences.keys.reduce(Long::times)
        return coprimeCongruences.entries.sumOf { (m, r) ->
            val M = product / m
            val N = M.modularInverse(m)
            r * M * N
        }
    }

    private fun findCycleCongruence(turnOrder: TurnOrder, nodeMap: NodeMap, startNode: String): TripleOf<CycleNumber> {
        var currNode = startNode
        val seen = mutableMapOf<Pair<String, Int>, CycleNumber>()
        var step = 0L
        while (true) {
            val turnIndex = (step % turnOrder.size).toInt()
            val turn = turnOrder[turnIndex]
            val nextNodes = nodeMap.getValue(currNode)
            currNode = if (turn == 'L') nextNodes.first else nextNodes.second
            step++
            val key = currNode to turnIndex
            val firstSeen = seen[key]
            if (firstSeen == null) {
                seen[key] = step
                continue
            }
            seen.entries.firstOrNull { it.key.first.endsWith('Z') }
                ?.let {
                    return Triple(firstSeen, step - firstSeen, it.value - firstSeen)
                }
        }
    }

    override fun getInput(handler: InputHandler): Pair<TurnOrder, NodeMap> {
        val (turns, nodes) = handler.getInput("\n\n")
        val nodeMap = nodes.split('\n')
            .map { it.split(" = ") }
            .associate { pieces ->
                val source = pieces.first()
                val dests = pieces.last().trim('(', ')').split(", ")
                source to (dests.first() to dests.last())
            }
        return turns.toCharArray() to nodeMap
    }

    override fun solve(input: Pair<TurnOrder, NodeMap>): PairOf<CycleNumber?> {
        val (turnOrder, nodeMap) = input

        var ans1: CycleNumber? = null
        val congruences = mutableListOf<PairOf<CycleNumber>>()
        nodeMap.keys.filter { it.endsWith('A') }
            .forEach {
                val (start, length, offset) = findCycleCongruence(turnOrder, nodeMap, it)
                congruences.add(length to start + offset)
                if (it == "AAA") ans1 = start + offset
            }

        val combinedCycle = congruences.map(PairOf<CycleNumber>::first).reduce(::lcm)
        var crt = chineseRemainderTheorem(congruences)
        if (crt != null) {
            crt %= combinedCycle
            if (crt <= 0) crt += combinedCycle
        }

        return ans1 to crt
    }
}
