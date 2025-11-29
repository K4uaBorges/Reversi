package model.piece

/**
 * @author Kauã Borges
 * @file Position.kt
 *
 * This file defines the data class Position, which represents a single
 * coordinate on the game board, defined by a row and a column.
 *
 * Functions:
 *  @see toString() ->
 *      Returns a formatted string representing the position,
 *      allowing easy printing and debugging of board coordinates.
 *
 *  @see Operator fun plus (dir: Directions) ->
 *      Overloads the plus operator to allow
 *      arithmetic movement of positions based on directional input,
 *
 *
 * ---
 * This part acts as a **fundamental component** of the board system,
 * serving as the reference point*
*/

enum class Directions(val dRow: Int, val dCol: Int) {
    LEFT(0, -1),
    RIGHT(0, 1),
    UP(-1, 0),
    DOWN(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

}



data class Position(val row:Int, val col: Char){

    override fun toString(): String {
        return "$row$col"
    }

    operator fun plus(dir: Directions): Position {
        return Position(
            row + dir.dRow,
            (col.code + dir.dCol).toChar()
        )
    }
}