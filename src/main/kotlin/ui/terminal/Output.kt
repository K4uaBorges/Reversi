/**
 * @author Kauã Borges
 * @file Output.kt
 *
 * This file defines how the game board is displayed in the console.
 * It uses the Game.show() extension function to print the board,
 * the pieces, and optional target positions (possible moves).
 *
 * It also counts and displays the number of black and white pieces,
 * as well as the current player’s turn.
 *
 * ---
 * This part is the **visual core** of the console version,
 * ensuring that players can clearly understand the board state
 * and make informed moves during the game.
 */


package ui.terminal

import game.Game
import game.piece.color.ColorEnum.*
import game.piece.position.*
import game.validMoves

const val BOARD_SIZE_LINE = 8
const val BOARD_SIZE_COL = 'H'
const val BOARD_SIZE_EMPTY = '.'
const val BOARD_SIZE_TARGET = '*'
const val BOARD_SIZE_BLACK = '#'
const val BOARD_SIZE_WHITE = '@'
var TARGETS_POSITIONS: Set<Position>? = null


    //Output game using loops
fun Game.show() {
    val map = board.getMapBoard()
    val overlay: Set<Position>? = TARGETS_POSITIONS?.let { validMoves().toSet() }

    // Column headers
    print("  ")
    for (col in 'A' .. BOARD_SIZE_COL) print(" $col")
    println()

    // Move across the board lines
    for (row in 1..BOARD_SIZE_LINE) {
        print("$row ")
        for (col in 'A' .. BOARD_SIZE_COL) {
            val pos = Position(row, col)
            val piece = map[pos]
            val symbol = if (piece == null && overlay?.contains(pos) == true) BOARD_SIZE_TARGET
            else when (piece?.color?.color) {
                BLACK -> BOARD_SIZE_BLACK
                WHITE -> BOARD_SIZE_WHITE
                else -> BOARD_SIZE_EMPTY
            }
            print(" $symbol")
        }
        println()
    }

    // Count pieces by HashMap value
    val counts = map.values.groupingBy { it.color.color }.eachCount()
    val blackCount = counts[BLACK] ?: 0
    val whiteCount = counts[WHITE] ?: 0

    println("# = $blackCount | @ = $whiteCount")
    println("Turn: ${if (turnWhite) "@" else "#"}")
}
