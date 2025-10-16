package Game.Table

import Game.Piece.Position.Position
import Game.Pieces.Piece
import Game.Pieces.Position.Positions.Directions
import Game.Pieces.getColor
import Game.Pieces.getPosition

class Search(val table: Table) {

    fun searchInDiag(p: Piece, invert: Boolean): MutableList<Position> {
        val targetColor = p.getColor()
        val start = p.getPosition()
        val result = mutableListOf<Position>()

        // Irá varrer dependendo do invert se for inverte
        // Então varrerá da direita para cima e depois da esquerda para baixo
        val (dir1, dir2) = if (invert)
            Pair(Directions.UP_RIGHT, Directions.DOWN_LEFT)
        else
            Pair(Directions.UP_LEFT, Directions.DOWN_RIGHT)

        // --- direção 1 ---
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(dir1)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == targetColor) { closed = true; break }
                pos = pos.plus(dir1)
            }
            if (closed) result.addAll(acc)
        }

        // --- direção 2 ---
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(dir2)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == targetColor) { closed = true; break }
                pos = pos.plus(dir2)
            }
            if (closed) result.addAll(acc)
        }

        return result
    }

    fun searchInRow(p: Piece): MutableList<Position> {
        val color = p.getColor()
        val start = p.position
        val result = mutableListOf<Position>()

        // Primeiro varre as posições das peças na direita
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(Directions.RIGHT)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == color) { closed = true; break }
                pos = pos.plus(Directions.RIGHT)
            }
            if (closed) result.addAll(acc)
        }

        // Depois varre as posições das peças na esquerda
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(Directions.LEFT)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == color) { closed = true; break }
                pos = pos.plus(Directions.LEFT)
            }
            if (closed) result.addAll(acc)
        }

        return result
    }

    fun searchInCol(p: Piece): MutableList<Position> {
        val color = p.getColor()
        val start = p.position
        val result = mutableListOf<Position>()

        // Primeiro varre as posiçoes de cada peça em baixo
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(Directions.DOWN)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == color) { closed = true; break }
                pos = pos.plus(Directions.DOWN)
            }
            if (closed) result.addAll(acc)
        }

        // Primeiro varre as posições de cada peça em cima
        run {
            val acc = mutableListOf<Position>()
            var pos = start.plus(Directions.UP)
            var closed = false
            while (table.inBound(pos) && table.getPiece(pos) != null) {
                val pieceCur = table.getPiece(pos)!!
                acc.add(pos)
                if (pieceCur.getColor() == color) { closed = true; break }
                pos = pos.plus(Directions.UP)
            }
            if (closed) result.addAll(acc)
        }

        return result
    }
}