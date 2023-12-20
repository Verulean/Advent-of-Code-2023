package days

import adventOfCode.InputHandler
import adventOfCode.Solution

typealias FlipFlopState = MutableMap<String, Boolean>
typealias ConjunctionState = MutableMap<String, MutableMap<String, Boolean>>
typealias ModuleGraph = Map<String, Set<String>>

object Solution20 : Solution<Triple<ModuleGraph, FlipFlopState, ConjunctionState>>(AOC_YEAR, 20) {
    override fun getInput(handler: InputHandler): Triple<ModuleGraph, FlipFlopState, ConjunctionState> {
        val graph = mutableMapOf<String, Set<String>>()
        val flipFlops = mutableMapOf<String, Boolean>()
        val conjunctions = mutableMapOf<String, MutableMap<String, Boolean>>()
        handler.getInput("\n")
            .map { it.split(" -> ") }
            .map { it.first() to it.last().split(", ").toSet() }
            .forEach { (source, dests) ->
                val sourceName = when (source[0]) {
                    '%' -> {
                        flipFlops[source.drop(1)] = false
                        source.drop(1)
                    }

                    '&' -> {
                        conjunctions[source.drop(1)] = mutableMapOf()
                        source.drop(1)
                    }

                    else -> source
                }
                graph[sourceName] = dests
            }
        graph.entries.forEach { (source, dests) ->
            dests.forEach { conjunctions[it]?.set(source, false) }
        }
        return Triple(graph, flipFlops, conjunctions)
    }

    private fun ModuleGraph.invert(): ModuleGraph {
        val inverseGraph = mutableMapOf<String, MutableSet<String>>()
        this.entries.forEach { (source, dests) ->
            dests.forEach { dest ->
                if (dest in inverseGraph) inverseGraph.getValue(dest).add(source)
                else inverseGraph[dest] = mutableSetOf(source)
            }
        }
        return inverseGraph
    }

    override fun solve(input: Triple<ModuleGraph, FlipFlopState, ConjunctionState>): Pair<Int, Long> {
        val (graph, flipFlops, conjunctions) = input
        // Part 1
        val pulses = mutableMapOf(false to 0, true to 0)
        val queue = ArrayDeque<Triple<String, String, Boolean>>()
        for (t in 1..1000) {
            queue.add(Triple("button", "broadcaster", false))
            while (queue.isNotEmpty()) {
                val (sender, receiver, state) = queue.removeFirst()
                pulses.merge(state, 1, Int::plus)
                var newState = state
                if (receiver in flipFlops) {
                    if (state) continue
                    newState = !flipFlops.getValue(receiver)
                    flipFlops[receiver] = newState
                } else conjunctions[receiver]?.let { states ->
                    states[sender] = state
                    newState = !states.values.all { it }
                }
                graph[receiver]?.forEach { queue.add(Triple(receiver, it, newState)) }
            }
        }
        // Part 2
        val inverseGraph = graph.invert()
        val binaryEncoders = inverseGraph.getValue(inverseGraph.getValue("rx").first())
            .flatMap(inverseGraph::getValue)
            .toSet()
        val ans2 = graph.getValue("broadcaster").map { counterStart ->
            var curr = counterStart
            val bits = mutableListOf<Boolean>()
            while (curr !in binaryEncoders) {
                val receivers = graph.getValue(curr)
                val conj = receivers.intersect(binaryEncoders)
                val flip = receivers - binaryEncoders
                bits.add(conj.isNotEmpty())
                curr = flip.firstOrNull() ?: conj.first()
            }
            bits.reversed().fold(0L) { acc, b -> acc.shl(1) + if (b) 1 else 0 }
        }.reduce(Long::times)
        return pulses.values.reduce(Int::times) to ans2
    }
}
