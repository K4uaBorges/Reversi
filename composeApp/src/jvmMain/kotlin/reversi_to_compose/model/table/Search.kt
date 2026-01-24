
package reversi_to_compose.model.table

import reversi_to_compose.model.piece.Directions.*
import reversi_to_compose.model.piece.Piece
import reversi_to_compose.model.piece.Directions
import reversi_to_compose.model.piece.Position
import java.util.HashMap

class Search(val table: Table) {

        // Search in Diagonal invert (UP RIGHT to DOWN LEFT) and not invert (UP LEFT to DOWN RIGHT)
    fun searchInDiag(p: Piece, invert: Boolean): HashMap<Position, Directions> {
        val targetColor = p.color
        val start = p.position
        val result = hashMapOf<Position, Directions>()


        val (dir1, dir2) = if (invert)
            Pair(UP_RIGHT, DOWN_LEFT)
        else
            Pair(UP_LEFT, DOWN_RIGHT)

            run {
                val acc = mutableListOf<Position>()
                var pos1 = start.plus(dir1)
                var pos2 = start.plus(dir2)
                var closed = false

                while (table.inBound(pos1) && table.getPiece(pos1) != null) {
                    if(table.getPiece(start.plus(dir1))!!.color == targetColor) { break }
                    val pieceCur = table.getPiece(pos1)!!
                    acc.add(pos1)
                    if (pieceCur.color == targetColor) { closed = true; break }
                    pos1 = pos1.plus(dir1)
                }

                if (closed) {
                    acc.forEach { result[it] = dir1 }
                }

                closed = false

                while (table.inBound(pos2) && table.getPiece(pos2) != null) {
                    if(table.getPiece(start.plus(dir2))!!.color == targetColor) { break }
                    val pieceCur = table.getPiece(pos2)!!
                    acc.add(pos2)
                    if (pieceCur.color == targetColor) { closed = true; break }
                    pos2 = pos2.plus(dir2)
                }

                if (closed) {
                    acc.forEach { result[it] = dir2 }
                }
            }

            return result
        }



    // Search in row if it doesn't have a piece in right, go search in a left, and the same in reverse
    fun searchInRow(p: Piece): HashMap<Position, Directions> {
        val targetColor = p.color
        val start = p.position
        val result = hashMapOf<Position, Directions>()


        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(RIGHT)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                if(table.getPiece(start.plus(RIGHT))!!.color == targetColor) { break }
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.color == targetColor) { closed = true; break }
                pos = pos.plus(RIGHT)
            }
            if (closed) {
                acc.forEach { result[it] = RIGHT }
            }
        }

        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(LEFT)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                if(table.getPiece(start.plus(LEFT))!!.color == targetColor) { break }
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.color == targetColor) { closed = true; break }
                pos = pos.plus(LEFT)
            }
            if (closed) {
                acc.forEach { result[it] = LEFT }
            }
        }

        return result
    }

        // Search in column if it doesn't have some any piece in down search to up, and same in reverse
    fun searchInCol(p: Piece): HashMap<Position, Directions> {
        val targetColor = p.color
        val start = p.position
        val result = hashMapOf<Position, Directions>()

        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(DOWN)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                if(table.getPiece(start.plus(DOWN))!!.color == targetColor) { break }
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.color == targetColor) { closed = true; break }
                pos = pos.plus(DOWN)
            }
            if (closed) {
                acc.forEach { result[it] = DOWN }
            }
        }

        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(UP)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                if(table.getPiece(start.plus(UP))!!.color == targetColor) { break }
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.color == targetColor) { closed = true; break }
                pos = pos.plus(UP)
            }
            if (closed) {
                acc.forEach { result[it] = UP }
            }
        }

        return result
    }
}