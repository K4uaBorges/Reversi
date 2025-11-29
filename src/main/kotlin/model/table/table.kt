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

package model.table

import model.piece.Color
import model.piece.Directions.*
import model.piece.Piece
import model.piece.Position

const val BOARD_SIZE: Int = 8

const val MIN_ROW: Int = 1
const val MIN_COL: Char = 'A'
const val MAX_ROW: Int = BOARD_SIZE

//Adding more 64, because the ascii code of the first letter start in 65 ("A")
const val MAX_COL: Char = (BOARD_SIZE+64).toChar()
const val TOTAL_BOARD_SIZE: Int = BOARD_SIZE*BOARD_SIZE

open class Table {

    // Table 8x8
    private val mapBoard = HashMap<Position, Piece>(BOARD_SIZE)

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