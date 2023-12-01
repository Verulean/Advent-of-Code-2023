import days.Solutions

fun main(args: Array<String>) {
    val indices = Solutions.indices
    args.map(String::toIntOrNull).forEach {
        if (it == null || it - 1 !in indices) return
        println("Day $it:")
        Solutions[it - 1].run()
    }
}
