package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints
import kotlin.math.max
import kotlin.math.min

typealias WorkflowCondition = Triple<String, Int, PairOf<Int>>
typealias WorkflowMap = Map<String, List<WorkflowCondition>>

object Solution19 : Solution<Pair<WorkflowMap, List<List<Int>>>>(AOC_YEAR, 19) {
    override fun getInput(handler: InputHandler): Pair<WorkflowMap, List<List<Int>>> {
        val pieces = handler.getInput("\n\n")
        val workflows = mutableMapOf<String, List<WorkflowCondition>>()
        pieces.first().split('\n').forEach workflowLoop@{ line ->
            val source = line.substringBefore('{')
            val conditions = mutableListOf<WorkflowCondition>()
            line.substringAfter('{')
                .substringBefore('}')
                .split(',')
                .map { it.split(':') }
                .forEach { pieces ->
                    if (pieces.size == 1) {
                        conditions.add(Triple(pieces[0], -1, -1 to -1))
                        workflows[source] = conditions
                        return@workflowLoop
                    }
                    val condition = pieces[0]
                    val dest = pieces[1]
                    val index = "xmas".indexOf(condition[0])
                    val value = condition.substring(2).toInt()
                    conditions.add(
                        Triple(
                            dest,
                            index,
                            when (condition[1]) {
                                '<' -> 1 to value
                                else -> value + 1 to 4001
                            }
                        )
                    )
                }
        }
        val parts = pieces.last().split('\n').map(String::ints)
        return workflows to parts
    }

    private fun mapWorkflows(workflows: WorkflowMap): List<List<PairOf<Int>>> {
        val validCombinations = mutableListOf<List<PairOf<Int>>>()
        val queue = ArrayDeque<Pair<String, List<PairOf<Int>>>>()
        queue.add("in" to List(4) { 1 to 4001 })
        do {
            val (curr, bounds) = queue.removeLast()
            when (curr) {
                "R" -> continue
                "A" -> {
                    validCombinations.add(bounds)
                    continue
                }
            }
            val remainingBounds = bounds.toMutableList()
            for ((next, i, r) in workflows.getValue(curr)) {
                if (i == -1) {
                    queue.add(next to remainingBounds)
                    break
                }
                val (a1, b1) = r
                val (a2, b2) = remainingBounds[i]
                val intersection = max(a1, a2) to min(b1, b2)
                if (intersection.first < intersection.second) {
                    val passed = remainingBounds.toMutableList()
                    passed[i] = intersection
                    queue.add(next to passed)
                }
                val difference = when {
                    b2 <= a1 -> a2 to b2
                    a2 >= b1 -> a2 to b2
                    b2 <= b1 -> a2 to a1
                    else -> b1 to b2
                }
                if (difference.first >= difference.second) break
                remainingBounds[i] = difference
            }
        } while (queue.isNotEmpty())
        return validCombinations
    }

    override fun solve(input: Pair<WorkflowMap, List<List<Int>>>): Pair<Int, Long> {
        val (workflows, parts) = input
        val validCombinations = mapWorkflows(workflows)
        val ans1 = parts.filter { part ->
            validCombinations.any { combination -> combination.zip(part).all { (r, p) -> p in r.first until r.second } }
        }.flatten().sum()
        val ans2 = validCombinations.sumOf { combination ->
            combination.map { it.second - it.first }.fold(1L, Long::times)
        }
        return ans1 to ans2
    }
}
