package game.table

import game.piece.color.Color
import game.piece.color.ColorEnum
import game.piece.position.Position
import game.piece.Piece
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SearchTest {

    val gameTest: Table = Table()
    val search = Search(gameTest)

    @Test
    fun test_if_search_in_climb_a_piece_with_table_true() {
        //Fulling all maps with piece for test and search in a diagonal white piece and return a map with this.

        gameTest.putPiece(pos = Position(3,'E'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(2,'F'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(3,'C'), c = Color(ColorEnum.BLACK))

        gameTest.changeColor(pos = Position(2, 'F'), c = Color(ColorEnum.WHITE))

        gameTest.putPiece(pos = Position(4,'D'), c = Color(ColorEnum.WHITE))

        val p1: Piece? = gameTest.getPiece(pos = Position(4, 'D'))

        assertEquals(
            mutableListOf
                ( Position(3, 'E'), Position(2, 'F')),
            search.searchInDiag(p1!!, true).keys.toList()
        )
    }

    @Test
    fun test_if_search_in_climb_a_piece_with_table_false() {
        //Fulling all maps with piece for test and search in a diagonal white piece and return a map with this.

        gameTest.putPiece(pos = Position(2,'B'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(3,'C'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(5,'E'), c = Color(ColorEnum.BLACK))

        gameTest.changeColor(pos = Position(2, 'B'), c = Color(ColorEnum.WHITE))

        gameTest.putPiece(pos = Position(4,'D'), c = Color(ColorEnum.WHITE))


        val p1: Piece? = gameTest.getPiece(pos = Position(4, 'D'))

        assertEquals(
            mutableListOf
                ( Position(3, 'C'), Position(2, 'B')),
            search.searchInDiag(p1!!, false).keys.toList()
        )
    }

    @Test
    fun test_if_search_in_same_row_a_piece_with_table() {

        gameTest.putPiece(pos = Position(4,'B'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'C'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'E'), c = Color(ColorEnum.BLACK))


        gameTest.changeColor(pos = Position(4, 'B'), c = Color(ColorEnum.WHITE))

        gameTest.putPiece(pos = Position(4,'D'), c = Color(ColorEnum.WHITE))

        val p1: Piece? = gameTest.getPiece(pos = Position(4, 'D'))

        assertEquals(
            mutableListOf
                (Position(4, 'B'), Position(4, 'C')),
            search.searchInRow(p1!!).keys.toList()
        )
    }

    @Test
    fun test_if_search_in_same_col_a_piece_with_table() {
        gameTest.putPiece(pos = Position(2,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(3,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(5,'D'), c = Color(ColorEnum.BLACK))



        gameTest.changeColor(pos = Position(2, 'D'), c = Color(ColorEnum.WHITE))

        gameTest.putPiece(pos = Position(4,'D'), c = Color(ColorEnum.WHITE))

        val p1: Piece? = gameTest.getPiece(pos = Position(4, 'D'))

        assertEquals(
            mutableListOf
                (Position(3,'D'), Position(2, 'D')),
            search.searchInCol(p1!!).keys.toList()
        )
    }

    @Test
    fun test_if_search_in_same_col_a_2_pieces_in_table() {

        gameTest.putPiece(pos = Position(1,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(2,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(3,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(5,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(6,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(7,'D'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(8,'D'), c = Color(ColorEnum.BLACK))

        gameTest.putPiece(pos = Position(4, 'D'), c = Color(ColorEnum.WHITE))

        gameTest.changeColor(pos = Position(2, 'D'), c = Color(ColorEnum.WHITE))
        gameTest.changeColor(pos = Position(8, 'D'), c = Color(ColorEnum.WHITE))

        val p1: Piece = gameTest.getPiece(pos = Position(4, 'D'))!!

        assertEquals(
            mutableListOf
                ( Position(3, 'D'), Position(2, 'D'), Position(8, 'D'),Position(7, 'D'),
                Position(6,'D'), Position(5, 'D'))
        ,search.searchInCol(p1).keys.toList()
        )
    }
    @Test
    fun test_if_search_in_same_row_a_2_pieces_in_table() {

        gameTest.putPiece(pos = Position(4,'A'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'B'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'C'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'E'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'F'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'G'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(4,'H'), c = Color(ColorEnum.BLACK))

        gameTest.putPiece(pos = Position(4, 'D'), c = Color(ColorEnum.WHITE))

        gameTest.changeColor(pos = Position(4, 'A'), c = Color(ColorEnum.WHITE))
        gameTest.changeColor(pos = Position(4, 'H'), c = Color(ColorEnum.WHITE))

        val p1: Piece = gameTest.getPiece(pos = Position(4, 'D'))!!

        assertEquals(
            mutableListOf
                (Position(4,'E'), Position(4, 'F'), Position(4, 'G'), Position(4, 'H'),
                    Position(4, 'A'), Position(4, 'B'), Position(4, 'C'))
            ,search.searchInRow(p1).keys.toList()
        )
    }

    @Test
    fun test_if_search_in_same_diag_a_2_pieces_in_table_false() {

        gameTest.putPiece(pos = Position(1,'A'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(2,'B'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(3,'C'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(5,'E'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(6,'F'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(7,'G'), c = Color(ColorEnum.BLACK))
        gameTest.putPiece(pos = Position(8,'H'), c = Color(ColorEnum.BLACK))

        gameTest.putPiece(pos = Position(4, 'D'), c = Color(ColorEnum.WHITE))

        gameTest.changeColor(pos = Position(1, 'A'), c = Color(ColorEnum.WHITE))
        gameTest.changeColor(pos = Position(8, 'H'), c = Color(ColorEnum.WHITE))

        val p1: Piece = gameTest.getPiece(pos = Position(4, 'D'))!!

        assertEquals(
            mutableListOf
                (Position(3,'C'), Position(2, 'B'), Position(1, 'A'),
                    Position(5, 'E'), Position(6, 'F'), Position(7, 'G'), Position(8, 'H'))
            ,search.searchInDiag(p1,false).keys.toList()
        )
    }

}