import adventOfCode.Solution
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

fun main(args: Array<String>) {
    args.map(String::toIntOrNull).forEach {
        try {
            val solution = Class.forName("days.Solution%02d".format(it)).kotlin.objectInstance
            if (solution is Solution<*>) {
                println("Day $it:")
                val duration = measureNanoTime {
                    solution.run()
                }
                println("Finished execution in ${duration.nanoseconds}.")
            }
        } catch (_: ClassNotFoundException) {
        }
    }
}
