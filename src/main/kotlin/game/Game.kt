/**
 * @author Kauã Borges
 * @file Game.kt
 *
 * This is the main file responsible for the core functionality of the game.
 * All game commands and logic—such as playing, saving, loading, passing turns,
 * and determining the winner—are implemented here.
 *
 * In short, this file is the heart of the game system, ensuring that all
 * interactions and mechanics work properly.
 */

package game

import game.table.Search
import game.piece.color.*
import game.piece.position.Position
import game.piece.Piece
import game.piece.color.ColorEnum.BLACK
import game.piece.color.ColorEnum.WHITE
import game.table.*
import kotlin.system.exitProcess


var passCount = 0
const val MAX_BOARD_CELL = 64
private const val MIN_ROW: Int = 1
private const val MIN_COL: Char = 'A'
private const val MAX_ROW: Int = 8
private const val MAX_COL: Char = 'H'


data class Game(
    var name: String?,
    var turnWhite: Boolean,
    var currentPlayer: Piece?,
    var board: Table,
    val playerColor: Color?
)

    /**
     *  Private Function to see all positions in board,
     *  @see validMovesFor is helping by this function
     *
     * */
private fun allBoardPositions(): Sequence<Position> = sequence {
    for (r in MIN_ROW ..MAX_ROW) for (c in MIN_COL..MAX_COL)
        yield(Position(r, c))
}


    // Function to init game
fun Game.new(): Game{
    board.clear()

    board.putPiece(Position(4,'D'), Color(BLACK))
    board.putPiece(Position(5,'E'), Color(BLACK))
        board.putPiece(Position(4,'E'), Color(WHITE))
        board.putPiece(Position(5,'D'), Color(WHITE))

    currentPlayer = Piece(Position(4,'E'), Color(WHITE))
    turnWhite = true

    if(name != null) {
        save()
    }
    return this
}

    // Function to playing
fun Game.play(pos: Position): Game {

    if (turnWhite && playerColor == Color(WHITE)
        || (!turnWhite && playerColor == Color(BLACK)) || name == null) {
        if (turnWhite){
            if (putPiece(pos)) {
                turnWhite = false
                currentPlayer = Piece(position = pos, color = Color(WHITE))
            } else {
                throw Error("INVALID POSITION $pos")
            }
        } else {
            if (putPiece(pos)) {
                turnWhite = true
                currentPlayer = Piece(position = pos, color = Color(BLACK))
            } else {
                throw Error("INVALID POSITION $pos")
            }
        }
    } else throw Error("YOU CAN'T PLAY, IT'S NOT YOUR TURN")
    return this
}

    // Finish game, and announcing a winner
fun Game.finish(){
    val counts = board.getMapBoard().values.groupingBy { it.color.color }.eachCount()
    val blackCount = counts[BLACK] ?: 0
    val whiteCount = counts[WHITE] ?: 0

    when (isPlayerWinner(blackCount, whiteCount)) {
        0 -> println("Player with Color $WHITE is Winner with $whiteCount against $blackCount \n Thank you for playing")
        1 -> println("Player with Color $BLACK is Winner with $blackCount against $whiteCount\n Thank you for playing")
        2 -> println("Nobody is winner, it's a Draw with black:$blackCount against whites:$whiteCount\n Thank you for playing")
        else -> throw Error("Invalid game")
    }
    exitProcess(0)
}

    // Save a game in file for using a load
fun Game.save() {

    if(name != null) {
        val gameSettings = GameSettings(name!!)
        gameSettings.save(this)
    } else throw Error("Cannot save game because is a local game")
}

    // Load a game for another play entry with command JOIN, in another terminal
fun Game.load(): Game {
    if(name != null) {
        val newGame:Game
        val gameSettings = GameSettings(name!!)
        board.clear()
        newGame = gameSettings.load(this)
        return newGame
    }  else throw Error("Cannot load game is a local game")
    return this
}

    // Just pass if a player doesn't have any play
fun Game.pass() {
    val moves = validMoves()

    if (moves.isNotEmpty()) {
        // No pass if you have any place to put a piece
        throw Error("You can't Pass, try to play in ${moves.first()}")
    }

    passCount++
    turnWhite = !turnWhite
    if (passCount >= 2) {
        // End a game, if both Players pass
        println("Sem jogadas. Foi passada a vez. (passes consecutivos: $passCount)")
        finish()
    } else {
        println("Sem jogadas. Passaste a vez.")
    }
}

    /**
     * Just are function for determine who is winner?
     * 0 -> White winner
     * 1 -> Black Winner
     * 2 -> Draw
     * -1 -> Error
     *
     * IsPlayerWinner helping the function
     * @param finish
     * To determine who is winner
     *
     * */

fun isPlayerWinner(black: Int, white: Int): Int = when {
    white > black -> 0
    black > white -> 1
    black == white -> 2
    else -> -1
}


    // Place a piece and return a boolean value to tell if it was done successfully
fun Game.putPiece(pos: Position) : Boolean{

    currentPlayer =
        Piece(position = pos, color = if(turnWhite)Color(WHITE) else Color(BLACK))

    if (!board.isValidPosition(pos,currentPlayer!!)) return false

    // Check if this position is valid and get pieces to flip in one go
    val positionsToFlip = getPositionsToFlip(currentPlayer!!)
    
    if (positionsToFlip.isNotEmpty()) {
        // Place the current player's piece
        board.putPiece(currentPlayer!!.position, currentPlayer!!.color)
        
        // Change color of all positions that should be flipped
        for (position in positionsToFlip) {
            board.changeColor(position, currentPlayer!!.color)
        }
        // Reset the number of passes
        passCount = 0

        return true
    }
    return false
}

    // Search all positions for you can flip the piece and return a list of all positions
fun Game.getPositionsToFlip(p: Piece): MutableList<Position> {
    val search = Search(board)
    val testPiece = Piece(p.position, p.color)
    
    val listDiagInvert = search.searchInDiag(testPiece, true)
    val listDiagNotInvert = search.searchInDiag(testPiece, false)
    val listRow = search.searchInRow(testPiece)
    val listCol = search.searchInCol(testPiece)
    
    // Combine all positions that should be flipped
    val allPositionsToFlip = mutableListOf<Position>()
    allPositionsToFlip.addAll(listDiagInvert.keys)
    allPositionsToFlip.addAll(listDiagNotInvert.keys)
    allPositionsToFlip.addAll(listRow.keys)
    allPositionsToFlip.addAll(listCol.keys)
    
    return allPositionsToFlip
}

    // List of valid Plays
fun Game.validMovesFor(color: Color): List<Position> {
    val moves = mutableListOf<Position>()
    for (pos in allBoardPositions()) {
        // Create a temporary piece for valid a plays
        val tmpPiece = Piece(pos, color)
        // The position is valid, if is flip a piece
        if (board.isValidPosition(pos, tmpPiece) && getPositionsToFlip(tmpPiece).isNotEmpty()) {
            moves.add(pos)
        }
    }
    return moves
}

    // List of valids Play, for you can put a piece
fun Game.validMoves(): List<Position> =
    validMovesFor(if (turnWhite) Color(WHITE) else Color(BLACK))