package Game.Piece.Position

import Game.Pieces.Position.Positions.Directions

data class Position(val row:Int, val col: Char){

    fun Position.isEqualRow(row: Int): Boolean { return this.row == row }

    fun Position.isEqual(col: Char): Boolean { return this.col == col }

    operator fun plus(dir: Directions): Position {
        return Position(
            row + dir.dRow,
            (col.code + dir.dCol).toChar()
        )
    }
}