package Game.Pieces

import Game.Piece.Position.Position
import Game.Piece.Color.Color

/**
 * No Reversi muda-se a cor, não a posição.
 * Cada peça tem uma posição fixa, e o jogador não se move.
 * Como a peça é fixa entao só foi definida uma posiçao unica, possiveis alteraçoes na cor
 */


data class Piece(val position: Position,var color: Color)

fun Piece.getPosition(): Position { return this.position }

fun Piece.setColor(color: Color) { this.color = color }

fun Piece.getColor(): Color { return this.color }
