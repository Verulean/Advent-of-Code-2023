import adventOfCode.Solution

fun main(args: Array<String>) {
    args.map(String::toIntOrNull).forEach {
        try {
            val solution = Class.forName("days.Solution%02d".format(it)).kotlin.objectInstance
            if (solution is Solution<*>) {
                println("Day $it:")
                solution.run()
            }
        } catch (_: ClassNotFoundException) {
        }
    }
}
