/**
 * @author Kauã Borges
 * @file Directions.kt
 *
 * This file defines the Directions enum/class, used to represent
 * movement directions on the game board.
 * This file supports positional logic, making board traversal
 * and piece movement intuitive and organized.
 */


package game.piece.position

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
