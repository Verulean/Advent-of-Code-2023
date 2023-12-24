package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.longs
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.sum

data class Hailstone(val x: Long, val y: Long, val z: Long, val vx: Long, val vy: Long, val vz: Long) {
    constructor(numbers: List<Long>) : this(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5])

    val m = vy.toDouble() / vx
}

object Solution24 : Solution<List<Hailstone>>(AOC_YEAR, 24) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { Hailstone(it.longs()) }

    private fun intersects(a: Hailstone, b: Hailstone): Boolean {
        if (a.vx * b.vy == a.vy * b.vx) return false
        val validRange = 2e14..4e14
        val x = (b.y - a.y + a.m * a.x - b.m * b.x) / (a.m - b.m)
        val y = a.y + a.m * (x - a.x)
        return (x in validRange
            && y in validRange
            && (x - a.x) / a.vx >= 0
            && (x - b.x) / b.vx >= 0
            )
    }

    private fun findRock(hailstones: List<Hailstone>): Long {
        val (h1, h2, h3) = hailstones.take(3)
        val a = mk.ndarray(
            mk[
                mk[0, h2.vz - h1.vz, h1.vy - h2.vy, 0, h1.z - h2.z, h2.y - h1.y],
                mk[h1.vz - h2.vz, 0, h2.vx - h1.vx, h2.z - h1.z, 0, h1.x - h2.x],
                mk[h2.vy - h1.vy, h1.vx - h2.vx, 0, h1.y - h2.y, h2.x - h1.x, 0],
                mk[0, h3.vz - h2.vz, h2.vy - h3.vy, 0, h2.z - h3.z, h3.y - h2.y],
                mk[h2.vz - h3.vz, 0, h3.vx - h2.vx, h3.z - h2.z, 0, h2.x - h3.x],
                mk[h3.vy - h2.vy, h2.vx - h3.vx, 0, h2.y - h3.y, h3.x - h2.x, 0],
            ]
        )
        val b = mk.ndarray(
            mk[
                h2.y * h2.vz - h2.z * h2.vy - h1.y * h1.vz + h1.z * h1.vy,
                h2.z * h2.vx - h2.x * h2.vz - h1.z * h1.vx + h1.x * h1.vz,
                h2.x * h2.vy - h2.y * h2.vx - h1.x * h1.vy + h1.y * h1.vx,
                h3.y * h3.vz - h3.z * h3.vy - h2.y * h2.vz + h2.z * h2.vy,
                h3.z * h3.vx - h3.x * h3.vz - h2.z * h2.vx + h2.x * h2.vz,
                h3.x * h3.vy - h3.y * h3.vx - h2.x * h2.vy + h2.y * h2.vx,
            ]
        )
        return mk.linalg.solve(a, b)[0..2].sum().toLong()
    }

    override fun solve(input: List<Hailstone>): Pair<Int, Long> {
        val ans1 = input.withIndex().sumOf { (i, a) -> input.drop(i + 1).filter { intersects(a, it) }.size }
        val ans2 = findRock(input)
        return ans1 to ans2
    }
}
