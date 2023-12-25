package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

typealias ComponentGraph = SimpleGraph<String, DefaultEdge>

object Solution25 : Solution<ComponentGraph>(AOC_YEAR, 25) {
    override fun getInput(handler: InputHandler): ComponentGraph {
        val graph = ComponentGraph(DefaultEdge::class.java)
        handler.getInput("\n").forEach { line ->
            val (src, dests) = line.split(": ")
            graph.addVertex(src)
            dests.split(' ').forEach {
                graph.addVertex(it)
                graph.addEdge(src, it)
            }
        }
        return graph
    }

    override fun solve(input: ComponentGraph): Pair<Int, Any?> {
        val a = StoerWagnerMinimumCut(input).minCut().size
        val b = input.vertexSet().size - a
        return a * b to null
    }
}
