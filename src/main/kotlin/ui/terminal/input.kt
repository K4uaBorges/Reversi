/**
 * @file input.kt
 *
 * I didn't do it, but I took it from another reading code in git
 */

package ui.terminal

data class LineCommand(val cmd: String, val args: List<String>)
fun readCommand(): LineCommand {
    print("$ ")
    val line = readln().uppercase().split(' ')
    return LineCommand(line[0], line.drop(1))
}