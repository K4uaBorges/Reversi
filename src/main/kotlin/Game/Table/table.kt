/**
 * Classe table apenas serve para implementar algumas mecanicas do jogo,
 * Apenas um complemento de manípulo
 * Na qual Game será responsável pelas modificações e regras e o decorrer do jogo
 *
 * Implementou-se o tamanho maximo e possíveis do jogo:
 * @see inBound
 *
 * Implementou-se o retorno, implementação e mudança da cor da peça:
 * @see putPiece
 * @see getPiece
 * @see changeColor
 *
 * Implementou-se
 * A verificação de posição de uma determinada peça
 * @see isValidPosition
 */

package Game.Table

import Game.Piece.Color.Color
import Game.Piece.Position.Position
import Game.Pieces.Piece
import Game.Pieces.Position.Positions.Directions.*
import Game.Pieces.getColor
import java.util.HashMap

private const val MIN_ROW: Int = 1;
private const val MIN_COL: Char = 'A';
private const val MAX_ROW: Int = 8;
private const val MAX_COL: Char = 'H';

class Table {

    val size = 16

    // Tabuleiro 8x8
    val mapBoard = HashMap<Position, Piece>(size)

//    fun getMapBoard(): HashMap<Position, Piece> = mapBoard

    // Verifica se esta no tamanho do tabuleiro
    fun inBound(pos: Position): Boolean {
        return pos.row >= MIN_ROW && pos.row <= MAX_ROW && pos.col >= MIN_COL && pos.col <= MAX_COL
            throw Error("INVALID POSITION $pos")
    }

    // Retorna uma peça
    fun getPiece(pos: Position): Piece? {
        return mapBoard.get(pos)
    }

    // Coloca uma peça
    fun putPiece(pos: Position, c: Color) {
        if(getPiece(pos) == null) mapBoard.put(pos, Piece( pos,c)) else null
    }

    // Efeito
    fun changeColor(pos: Position,c: Color){
        val newPiece = Piece(pos,c)
        mapBoard.replace(pos,newPiece)
    }

    fun isValidPosition(pos: Position, piece: Piece): Boolean {
        // Se posição fora do tabuleiro ou já ocupada -> inválida
        if (!inBound(pos) || (getPiece(pos) != null)) return false

        // Lista de direções possíveis de Directions no enum
        val directions = listOf(
            LEFT, RIGHT, UP, DOWN,
            UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
        )

        // Verifica se há vizinhos de cor diferente verificando em conjunto possiveis peças
        for (dir in directions) {
            val neighbor = getPiece(pos + dir)
            if (neighbor != null && neighbor.getColor() != piece.getColor()) {
                return true
            }
        }

        return false
    }
}