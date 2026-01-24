package reversi_to_compose.model.table

import reversi_to_compose.model.piece.Piece
import reversi_to_compose.model.piece.Color
import reversi_to_compose.model.piece.Directions.*
import reversi_to_compose.model.piece.Position
import java.util.HashMap

const val BOARD_SIZE: Int = 8
const val MIN_ROW: Int = 1
const val MIN_COL: Char = 'A'
const val MAX_ROW: Int = BOARD_SIZE
const val MAX_COL: Char = (BOARD_SIZE+64).toChar()
const val TOTAL_BOARD_SIZE: Int = BOARD_SIZE*BOARD_SIZE


data class Table(val mapBoard: HashMap<Position, Piece>) {

    // Get the Table
    fun getBoard(): HashMap<Position, Piece> = mapBoard

    // Clean Table
    fun clear() = mapBoard.clear()

    // Safe version of inBound that doesn't throw errors
    fun inBound(pos: Position): Boolean =
        pos.row >= MIN_ROW && pos.row <= MAX_ROW && pos.col >= MIN_COL && pos.col <= MAX_COL


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
            UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
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