package Game.Table

import Game.Piece.Color.Color
import Game.Piece.Color.ColorEnum.*
import Game.Piece.Position.Position
import Game.Pieces.Piece
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class tableTest {

    @Test
    fun testing_if_inBound() {
        val gameTest = Table()
        assertTrue(gameTest.inBound(Position(3, 'D')))
        assertTrue(gameTest.inBound(Position(1, 'A')))
        assertTrue(gameTest.inBound(Position(8, 'H')))
    }

    @Test
    fun testing_if_not_inBound() {
        val gameTest = Table()
        assertFalse(gameTest.inBound(Position(9, 'D')))
        assertFalse(gameTest.inBound(Position(0, 'A')))
    }

    @Test
    fun testing_if_put_Piece() {
        val gameTest = Table()

        assertNotNull(
            gameTest.putPiece(
            Position(3, 'D'),
            Color(WHITE))
        )

        assertNotNull(
            gameTest.putPiece(
                Position(3, 'A'),
                Color(BLACK))
        )
    }

    @Test
    fun testing_if_getting_Piece_and_return_null_if_is_not() {
        val gameTest = Table()

        gameTest.putPiece(
            Position(3, 'D'),
            Color(WHITE)
        )

        assertNotEquals(null,gameTest.getPiece(Position(3, 'D')))

        assertNull(gameTest.getPiece(Position(3, 'A')))
    }

    @Test
    fun testing_changing_Color() {
        val gameTest = Table()

        gameTest.putPiece(
            Position(3, 'D'),
            Color(WHITE)
        )

        val newPiece = gameTest.changeColor(
            c = Color(BLACK),
            pos = Position(3, 'D'))


        assertEquals(Piece(
            Position(3,'D'),
            color = Color(BLACK)),

            gameTest.getPiece(Position(3, 'D')))
    }

    @Test
    fun testing_if_valid_positions() {
        val gameTest = Table()
        gameTest.putPiece(Position(3, 'D'), Color(WHITE))

        assertFalse(gameTest.isValidPosition(Position(3, 'D'), Piece(Position(3, 'D'),Color(WHITE))))
        assertFalse(gameTest.isValidPosition(Position(3, 'A'), Piece(Position(3, 'A'),Color(WHITE))))
        assertTrue(gameTest.isValidPosition(Position(3, 'E'), Piece(Position(3, 'E'),Color(BLACK))))
        assertFalse(gameTest.isValidPosition(Position(4, 'D'), Piece(Position(4, 'D'),Color(WHITE))))
    }

}