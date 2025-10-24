package game

import game.piece.color.Color
import game.piece.color.ColorEnum
import game.piece.position.Position
import game.piece.Piece
import game.table.Table
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameTest {

    private lateinit var game: Game

    @BeforeEach
    fun setUp() {
        // Inicializa o jogo básico
        game = Game(
            "testGame",
            true,
            null,
            Table(),
            Color(ColorEnum.BLACK)
        )
        game.new()
    }

    // ---------- Teste do new() ----------
    @Test
    fun New_Should_Initialize_Correct_Starting_Pieces() {
        val black1 = game.board.getPiece(Position(4, 'D'))
        val black2 = game.board.getPiece(Position(5, 'E'))
        val white1 = game.board.getPiece(Position(4, 'E'))
        val white2 = game.board.getPiece(Position(5, 'D'))

        assertNotNull(black1)
        assertNotNull(black2)
        assertNotNull(white1)
        assertNotNull(white2)

        assertEquals(ColorEnum.BLACK, black1!!.color.color)
        assertEquals(ColorEnum.BLACK, black2!!.color.color)
        assertEquals(ColorEnum.WHITE, white1!!.color.color)
        assertEquals(ColorEnum.WHITE, white2!!.color.color)
    }

    // ---------- Teste do putPiece() ----------
    @Test
    fun PutPiece_Should_Add_A_New_Piece_When_Position_Is_Valid1() {
        val pos = Position(3, 'D')
        val result = game.putPiece(pos)
        assertTrue(result, "A peça deveria ser colocada com sucesso")
    }

    @Test
    fun PutPiece_Should_Add_A_New_Piece_When_Position_Is_Valid2() {
        val pos = Position(5, 'F')
        val result = game.putPiece(pos)
        assertTrue(result, "A peça deveria ser colocada com sucesso")
    }

    @Test
    fun PutPiece_Should_Return_False_When_Position_Is_Invalid_out_of_range() {
        val pos = Position(9, 'C') // inválido no tabuleiro - fora dos limites
        val result = game.putPiece(pos)
        assertFalse(result, "Deveria retornar falso pois a posição é inválida")
    }

    @Test
    fun PutPiece_Should_Return_False_When_Position_Is_Invalid_not_changeColor() {
        val pos = Position(3, 'C') // posição que não vira nenhuma peça
        val result = game.putPiece(pos)
        assertFalse(result, "Deveria retornar falso pois a posição é inválida")
    }

    // ---------- Teste do finish() ----------
    @Test
    fun Finish_Should_Not_Throw_Exception() {
        assertDoesNotThrow { game.finish() }
    }

    // ---------- Teste de save() e load() ----------
    @Test
    fun Save_And_Load_Should_Not_Throw_Exception() {
        assertDoesNotThrow {
            game.save()
            game.load()
        }
    }

    // ------ Testes de validMoves() ------
    @Test
    fun validMoves_Should_Not_Be_Empty_At_Game_Start() {
        val moves = game.validMoves()
        assertTrue(moves.isNotEmpty(), "O jogo inicial deve ter jogadas válidas.")
    }

    @Test
    fun validMoves_Should_Not_Include_Occupied_Positions() {
        val moves = game.validMoves()
        val occupied = game.board.getMapBoard().keys
        val containsOccupied = moves.any { it in occupied }
        assertFalse(containsOccupied, "Nenhuma jogada válida deve já estar ocupada no tabuleiro.")
    }

    @Test
    fun validMoves_Should_Not_Contain_OutOfBounds_Positions() {
        val moves = game.validMoves()
        val anyOutOfBounds = moves.any { !game.board.inBound(it) }
        assertFalse(anyOutOfBounds, "Nenhuma jogada válida deve estar fora do tabuleiro.")
    }

    @Test
    fun validMoves_At_Initial_Position_Should_Match_Expected_Set() {
        val moves = game.validMoves().toSet()
        val expected = setOf(
            Position(4,'C'),
            Position(3,'D'),
            Position(6,'E'),
            Position(5,'F')
        )
        assertEquals(expected, moves, "Jogadas válidas iniciais não correspondem ao esperado.")
    }

    @Test
    fun validMoves_Should_Be_Empty_On_Empty_Board() {
        val emptyGame = Game(
            name = "Empty",
            turnWhite = true,
            currentPlayer = Piece(Position(1, 'A'), Color(ColorEnum.WHITE)),
            board = Table(), // Tabuleiro vazio
            playerColor = Color(ColorEnum.BLACK)
        )
        val moves = emptyGame.validMoves()
        assertTrue(moves.isEmpty(), "Valid moves should be empty on empty board")
    }

    //----------- Testes da função pass() --------------

    @Test
    fun pass_Should_Throw_Error_When_Valid_Moves_Exist() {
        // No estado inicial há jogadas válidas -> deve lançar erro
        val ex = assertThrows<Error> { game.pass() }
        assertTrue(ex.message!!.contains("You can't Pass"))
    }

    @Test
    fun pass_Should_Switch_Turn_When_No_Valid_Moves() {
        // Criar um estado sem jogadas válidas para a cor do turno
        val emptyGame = Game(
            name = "EmptyNoMoves",
            turnWhite = true,
            currentPlayer = null,
            board = Table(),
            playerColor = Color(ColorEnum.BLACK)
        )
        // Tabuleiro vazio -> sem jogadas válidas
        assertTrue(emptyGame.validMoves().isEmpty())

        // Não deve lançar e deve alternar o turno
        assertDoesNotThrow { emptyGame.pass() }
        assertFalse(emptyGame.turnWhite, "Após o pass, o turno devia alternar.")
    }
}
