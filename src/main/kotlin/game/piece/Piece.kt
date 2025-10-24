/**
 * @author Kauã Borges
 * @file Piece.kt
 *
 * Data class Simples
 * @see Piece
 *
 * Each piece has a fixed position, and the player does not move, just adds
 * As the piece is fixed, only a single position has been defined, possible changes in color
 */

package game.piece

import game.piece.color.*
import game.piece.position.*

data class Piece(val position: Position, val color: Color)