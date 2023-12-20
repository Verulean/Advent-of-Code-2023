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

    override fun solve(input: Triple<ModuleGraph, FlipFlopState, ConjunctionState>): Pair<Any?, Any?> {
        val (graph, flipFlops, conjunctions) = input
        var lowPulses = 0
        var highPulses = 0
        val inverseGraph = graph.invert()
        val cycleStarts = inverseGraph.getValue(
            inverseGraph.getValue("rx").first()
        ).associateWith { -1 }.toMutableMap()
        var ans2 = 1L
        val queue = ArrayDeque<Triple<String, String, Boolean>>()
        for (t in 1..10000) {
            queue.add(Triple("button", "broadcaster", false))
            while (queue.isNotEmpty()) {
                val (sender, receiver, state) = queue.removeFirst()
                if (t <= 1000) when (state) {
                    true -> highPulses++
                    false -> lowPulses++
                }
                if (!state && receiver in cycleStarts) {
                    val prev = cycleStarts.getValue(receiver)
                    if (prev == -1) cycleStarts[receiver] = t
                    else {
                        ans2 *= t - prev
                        cycleStarts.remove(receiver)
                        if (cycleStarts.isEmpty()) return lowPulses * highPulses to ans2
                    }
                }
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
        throw Exception("unreachable")
    }
}
