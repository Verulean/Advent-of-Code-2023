package adventOfCode

import java.io.File
import java.net.URI

class InputHandler(val year: Int, val day: Int) {
    fun getInput(): String {
        // Check if we have the input file
        val path = ".\\input\\${"%02d".format(day)}.txt"
        val inputFile = File(path)
        if (inputFile.exists()) {
            return inputFile.readText().trimEnd('\n')
        }

        // Download file
        val sessionFile = File(".\\session.cookie")
        if (!sessionFile.exists()) {
            return ""
        }
        val url = URI("https://adventofcode.com/$year/day/$day/input").toURL()
        val connection = url.openConnection()
        connection.setRequestProperty("Cookie", "session=${sessionFile.readText().trim()}")
        connection.connect()
        val input = connection.getInputStream().bufferedReader().readText()

        // Save input to disk
        inputFile.parentFile.mkdirs()
        inputFile.writeText(input)
        return input.trimEnd('\n')
    }

    fun <T : Any> getInput(delimiter: String, trim: Boolean = true, transform: (String) -> T): List<T> {
        val data = getInput().split(delimiter)
        if (trim) {
            return data.map(String::trim).map(transform)
        }
        return data.map(transform)
    }

    fun getInput(delimiter: String, trim: Boolean = true): List<String> {
        return getInput(delimiter, trim) { it }
    }
}
