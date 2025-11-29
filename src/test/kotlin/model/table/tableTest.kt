package model.table

import model.piece.Color
import model.piece.ColorEnum.*
import model.piece.Piece
import model.piece.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TableTest {

    @Test
    fun testing_if_inBound() {
        val tableTest = Table()
        assertTrue(tableTest.inBound(Position(3, 'D')))
        assertTrue(tableTest.inBound(Position(1, 'A')))
        assertTrue(tableTest.inBound(Position(8, 'H')))
    }

    @Test
    fun testing_if_not_inBound() {
        val tableTest = Table()
        assertFalse{tableTest.inBound(Position(9, 'D'))}
        assertFalse{tableTest.inBound(Position(0, 'Z'))}
    }

    @Test
    fun testing_if_put_Piece() {
        val tableTest = Table()
        tableTest.putPiece(Position(3, 'D'), Color(WHITE))
        tableTest.putPiece(Position(3, 'A'), Color(BLACK))

        val expectedPiece1 = Piece(Position(3, 'D'), Color(WHITE))

        assertEquals(expectedPiece1,
            tableTest.getPiece(Position(3, 'D'))
        )

        val expectedPiece2 = Piece(Position(3, 'A'), Color(BLACK))

        assertEquals(expectedPiece2, tableTest.getPiece(Position(3, 'A')))

    }

    @Test
    fun testing_if_getting_Piece_and_return_null_if_is_not() {
        val tableTest = Table()

        tableTest.putPiece(
            Position(3, 'D'),
            Color(WHITE)
        )

        assertNotEquals(null,tableTest.getPiece(Position(3, 'D')))

        assertNull(tableTest.getPiece(Position(3, 'A')))
    }

    @Test
    fun testing_changing_Color() {
        val tableTest = Table()

        tableTest.putPiece(
            Position(3, 'D'),
            Color(WHITE)
        )

        // Actually change the color
        tableTest.changeColor(Position(3, 'D'), Color(BLACK))

        assertEquals(Piece(
            Position(3,'D'),
            color = Color(BLACK)),

            tableTest.getPiece(Position(3, 'D')))
    }

    @Test
    fun testing_if_valid_positions() {
        val tableTest = Table()
        tableTest.putPiece(Position(3, 'D'), Color(WHITE))

        assertFalse(tableTest.isValidPosition(Position(3, 'D'), Piece(Position(3, 'D'),Color(WHITE))))
        assertFalse(tableTest.isValidPosition(Position(3, 'A'), Piece(Position(3, 'A'),Color(WHITE))))
        assertTrue(tableTest.isValidPosition(Position(3, 'E'), Piece(Position(3, 'E'),Color(BLACK))))
        assertFalse(tableTest.isValidPosition(Position(4, 'D'), Piece(Position(4, 'D'),Color(WHITE))))
    }

}