/**
 * @author Kauã Borges
 * @file GameSettings.kt
 *
 * This file contains the @param GameSettings class,
 * responsible for managing the persistence of the game
 * -> including creating, saving, and reading files.
 *
 * The class allows players to save and resume their games without interruptions,
 * handling all file operations and data management required for this process.
 *
 * It is particularly important when using the "JOIN" command in the terminal,
 * allowing the game to be resumed and played on another terminal,
 * with the game state preserved, including the player’s color
 * and the arrangement of pieces on the board.
 *
 * The main functionalities include:
 *
 *  @see save(): saves the game data to a file,
 *      including the board state, the player’s color, and whose turn it is.
 *
 *  @see load(): loads a game from an existing file,
 *  checking the validity of the data and recreating the game state as necessary.
 */


package game

import game.piece.color.Color
import game.piece.color.ColorEnum.*
import game.piece.position.Position
import game.piece.Piece
import game.table.Table
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameSettingsTest {

    var gameTest = Game("testing",true,
        null, Table(), Color(BLACK))

    @Test
    fun test_SaveBoard_Writes_Correct_Format() {

        gameTest.new()
        gameTest.save()

        assertNotNull(gameTest.load())
    }

    @Test
    fun test_Load_Board_Reads_Correctly() {
        val expected = gameTest.new()
        assertEquals(expected.board.getMapBoard(), gameTest.board.getMapBoard())
    }


    @Test
    fun test_LoadBoard_Throws_Error_When_File_Does_Not_Exist() {
        gameTest = Game(
            name = "null", true, Piece(Position(3, 'A'),
            Color(BLACK)), Table(),Color(BLACK))

        val nonExisting = GameSettings(null)

        assertThrows<Error> {
            nonExisting.load(game = gameTest)
        }
    }



    @Test
    fun test_LoadBoard_Throws_Error_When_Change_Piece_Does_Not_Exist() {
        gameTest = Game(
            name = "testChanges", true, Piece(Position(3, 'A'),
                Color(BLACK)), Table(),Color(BLACK))

        //gameTest.save()

        assertThrows<Error> {
            gameTest.load()
        }
    }
}