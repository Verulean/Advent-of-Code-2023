package adventOfCode

abstract class Solution<T>(year: Int, day: Int) {
    open val inputHandler = InputHandler(year, day)
    abstract fun getInput(handler: InputHandler): T
    abstract fun solve(input: T): Pair<Any?, Any?>
    open fun run() {
        val (ans1, ans2) = solve(getInput(inputHandler))
        println(ans1)
        println(ans2)
    }
}
