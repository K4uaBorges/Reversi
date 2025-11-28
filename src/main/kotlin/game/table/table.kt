/**
 * @author Kauã Borges
 * @file Table.kt
 *
 * Table class is only used to implement some game mechanics,
 * Only one handle add-on
 * In which Game will be responsible for the modifications and rules and the course of the game
 *
 * The maximum and possible size of the game has been implemented:
 * @see inBound
 *
 * The getter, setter and changer colors:
 * @see putPiece
 * @see getPiece
 * @see changeColor
 *
 * Implemented
 * Checking the position of a given part
 * @see isValidPosition
*/

package game.table

import game.piece.color.*
import game.piece.position.Position
import game.piece.Piece
import game.piece.position.Directions.*
import java.util.HashMap

const val MIN_ROW: Int = 1
const val MIN_COL: Char = 'A'
const val MAX_ROW: Int = 8
const val MAX_COL: Char = 'H'
const val SIZE: Int = 64

open class Table {

    // Table 8x8
    private val mapBoard = HashMap<Position, Piece>(SIZE)

    // Get the Table
    fun getMapBoard(): HashMap<Position, Piece> = mapBoard

    // Clean Table
    fun clear() = mapBoard.clear()

    // Safe version of inBound that doesn't throw errors
    fun inBound(pos: Position): Boolean {
        return pos.row >= MIN_ROW && pos.row <= MAX_ROW && pos.col >= MIN_COL && pos.col <= MAX_COL
    }

    // Return a Piece
    fun getPiece(pos: Position): Piece? = mapBoard.get(pos)

    // Put a Piece
    fun putPiece(pos: Position, c: Color) =
        if(getPiece(pos) == null) mapBoard.put(pos, Piece( pos,c)) else null


    // Effect, Change a color
    fun changeColor(pos: Position,c: Color) {
        val newPiece = Piece(pos, c)
        mapBoard.replace(pos, newPiece)
    }

    // Check if is valid Position, return a boolean value if is successful or not
    fun isValidPosition(pos: Position, piece: Piece): Boolean {
        // If out of Bound -> return false
        if (!inBound(pos) || (getPiece(pos) != null)) return false

        val directions = listOf(
            LEFT, RIGHT, UP, DOWN,
            UP_LEFT, UP_RIGHT, DOWN_LEFT,DOWN_RIGHT
        )

        // Certify if have a neighbor piece
        for (dir in directions) {
            val neighbor = getPiece(pos + dir)
            if (neighbor != null && neighbor.color != piece.color) {
                return true
            }
        }

        return false
    }
}