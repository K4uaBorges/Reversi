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
package model

import model.piece.Color
import model.piece.ColorEnum.*
import model.piece.Piece
import model.piece.Position
import model.table.*
import model.table.Search
import storage.gameStorage


private var passCount = 0
private const val INICIAL_BOARD_INT = BOARD_SIZE /2

// add 64 because the first letter in alphabet (A) in ascii number start with 65
private const val INICIAL_BOARD_CHAR = ((INICIAL_BOARD_INT)+64).toChar()

data class Game(
    val name: String?,
    val turnWhite: Boolean,
    val currentPlayer: Piece,
    val board: Table,
    val playerColor: Color,
)


private fun allBoardPositions(): Sequence<Position> = sequence {
    for (r in MIN_ROW..MAX_ROW) for (c in MIN_COL..MAX_COL)
        yield(Position(r, c))
}


// Function to init game
fun Game.new(): Game{
    board.clear()

    board.putPiece(Position(INICIAL_BOARD_INT, INICIAL_BOARD_CHAR), Color(BLACK))
    board.putPiece(Position(INICIAL_BOARD_INT+1,INICIAL_BOARD_CHAR+1), Color(BLACK))
    board.putPiece(Position(INICIAL_BOARD_INT+1,INICIAL_BOARD_CHAR), Color(WHITE))
    board.putPiece(Position(INICIAL_BOARD_INT,INICIAL_BOARD_CHAR+1), Color(WHITE))

    if(name != null) {
        save()
    }

    return this.copy(
        turnWhite = true,
        currentPlayer =
            Piece(Position(INICIAL_BOARD_INT,INICIAL_BOARD_CHAR+1), Color(WHITE))
    )
}

// Function to playing
fun Game.play(pos: Position): Game {
    if ((turnWhite && playerColor.color == WHITE)
        || (!turnWhite && playerColor.color == BLACK) || name == null) {
        if (putPiece(pos)) {
            return this.copy(
                turnWhite = !turnWhite,
                currentPlayer =
                    Piece(position = pos, color = if(turnWhite) Color(WHITE) else Color(BLACK))
            )
        } else {
            throw Error("INVALID POSITION $pos")
        }
    } else throw Error("YOU CAN'T PLAY, IT'S NOT YOUR TURN")
}

// Save a game in file for using a load
fun Game.save(): Game {

    if(name != null) {
        val gameStorage = gameStorage(name)
        gameStorage.save(this)
    } else throw Error("Cannot save game because is a local game")
    return copy()
}

// Load a game for another play entry with command JOIN, in another terminal
fun Game.load(): Game {
    if(name != null) {
        val newGame: Game
        val gameStorage = gameStorage(name)
        board.clear()
        newGame = gameStorage.load(this)
        return newGame
    }  else throw Error("Cannot load game is a local game")
}

// Just pass if a player doesn't have any play
fun Game.pass(): Game {
    val moves = validMoves()

    if (moves.isEmpty()) {
        // No pass if you have any place to put a piece
        throw Error("You can't Pass, try to play in ${moves.first()}")
    }

    passCount++

    return copy(turnWhite = !turnWhite)
}

// Finish game, and announcing a winner
fun Game.finish(){
    val counts = board.getMapBoard().values.groupingBy { it.color.color }.eachCount()
    val blackCount = counts[BLACK] ?: 0
    val whiteCount = counts[WHITE] ?: 0

    return when (isPlayerWinner(blackCount, whiteCount)) {
        0 -> println("Player with Color $WHITE is Winner with $whiteCount against $blackCount \n Thank you for playing")
        1 -> println("Player with Color $BLACK is Winner with $blackCount against $whiteCount\n Thank you for playing")
        2 -> println("Nobody is winner, it's a Draw with black:$blackCount against whites:$whiteCount\n Thank you for playing")
        else -> throw Error("Invalid game")
    }
}


fun isPlayerWinner(black: Int, white: Int): Int = when
{
    white > black -> 0
    black > white -> 1
    black == white -> 2
    else -> -1
}


// Place a piece and return a boolean value to tell if it was done successfully
fun Game.putPiece(pos: Position) : Boolean{

    val tempPiece =
        Piece(position = pos, color = if(turnWhite)Color(WHITE) else Color(BLACK))


    if (!board.isValidPosition(pos,tempPiece)) return false

    // Check if this position is valid and get pieces to flip in one go
    val positionsToFlip = getPositionsToFlip(tempPiece)

    if (positionsToFlip.isNotEmpty()) {
        // Place the current player's piece
        board.putPiece(tempPiece.position, tempPiece.color)

        // Change color of all positions that should be flipped
        for (position in positionsToFlip) {
            board.changeColor(position, tempPiece.color)
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

    val listDiagInvert = search.searchInDiag(p, true)
    val listDiagNotInvert = search.searchInDiag(p, false)
    val listRow = search.searchInRow(p)
    val listCol = search.searchInCol(p)

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