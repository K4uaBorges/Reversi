package reversi_to_compose.model

import reversi_to_compose.model.piece.*
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.table.*
import reversi_to_compose.storage.StorageDB
import reversi_to_compose.storage.mongoDB.MongoStorage
import reversi_to_compose.storage.mongoDB.*
import java.util.HashMap



typealias Score = Map<Color?, Int>
private const val INICIAL_BOARD_INT = BOARD_SIZE / 2
private const val GAME_DOC_KEY = "state"

// add 64 because the first letter in alphabet (A) in ascii number start with 65
private const val INICIAL_BOARD_CHAR = ((INICIAL_BOARD_INT) + 64).toChar()

data class Game(
    val name: String?,
    val turnWhite: Boolean,
    val board: Table,
    val playerColor: Color,
    val score: Score = (Color.entries + null).associateWith { 0 }
)

val driver: MongoDriver = MongoDriver("ReversiKapa")        //nome da clucher em questao

fun Game.isValid() = name != null && name.isNotBlank() && name.all { it.isLetterOrDigit() }

private fun Score.advance(player: Color?): Score =
    this + (player to this.getValue(player) + 1)

private fun allBoardPositions(): Sequence<Position> = sequence {
    for (r in MIN_ROW..MAX_ROW) for (c in MIN_COL..MAX_COL)
        yield(Position(r, c))
}

// Function to init game
fun Game.new(): Game {

    val newBoard = Table(HashMap<Position, Piece>(TOTAL_BOARD_SIZE))

    newBoard.putPiece(Position(INICIAL_BOARD_INT, INICIAL_BOARD_CHAR), BLACK)
    newBoard.putPiece(Position(INICIAL_BOARD_INT + 1, INICIAL_BOARD_CHAR + 1), BLACK)
    newBoard.putPiece(Position(INICIAL_BOARD_INT + 1, INICIAL_BOARD_CHAR), WHITE)
    newBoard.putPiece(Position(INICIAL_BOARD_INT, INICIAL_BOARD_CHAR + 1), WHITE)

    return this.copy(
        turnWhite = true,
        board = newBoard
    )
}

// Function to playing
fun Game.play(pos: Position): Game {
    val newBoard = Table(HashMap<Position, Piece>(TOTAL_BOARD_SIZE))
    if ((turnWhite && playerColor == WHITE)
        || (!turnWhite && playerColor == BLACK) || name == null
    ) {
        if (putPiece(pos, newBoard)) {
            return this.copy(
                turnWhite = !turnWhite,
                board = newBoard
            )
        }

    }
    return this
}

fun Game.saveDB(): Game {
    val gameName = name ?: throw NoStorageOrModifierFileException()

    val gameStorage = MongoStorage<String,Game>(
        collectionName = gameName,
        driver = driver,
        serializer = StorageDB
    )

    // upsert manual (create se não existe, update se existe)
    val exists = gameStorage.read(GAME_DOC_KEY) != null
    if (!exists) gameStorage.create(GAME_DOC_KEY, this) else gameStorage.update(GAME_DOC_KEY, this)

    return copy()
}

fun Game.loadDB(): Game {
    val gameName = name ?: throw NoStorageOrModifierFileException()
    val gameStorage = MongoStorage<String,Game>(
        collectionName = gameName,
        driver = driver,
        serializer = StorageDB
    )

    val loaded = gameStorage.read(GAME_DOC_KEY)
        ?: throw NoStorageOrModifierFileException()

    return this.copy(
        turnWhite = loaded.turnWhite,
        playerColor = loaded.playerColor,
        board = loaded.board
    )
}

// Save a game in file for using a load
//fun Game.saveFile(): Game {
//    if (name != null) {
//        val gameStorage = StorageFile(name)
//        gameStorage.save(this)
//    } else throw TTTFatalException("No storage")
//    return copy()
//}
//
//fun Game.loadFile(): Game {
//    if (name != null) {
//        val newGame: Game
//        val gameStorage = StorageFile(name)
//        board.clear()
//        newGame = gameStorage.load(this)
//        return newGame
//    } else throw NoStorageOrModifierFileException()
//}

fun Game.pass(): Game {
    val moves = validMoves()

    if (moves.isNotEmpty()) {
        throw NoPassPossibleException()
    }

    return copy(turnWhite = !turnWhite)
}

fun Game.finish(blackCount: Int, whiteCount: Int): Game {

    return copy(score =
        when (isPlayerWinner(blackCount, whiteCount)) {
            0 -> score.advance(WHITE)
            1 -> score.advance(BLACK)
            else -> score.advance(null)
        }
    )
}

fun Game.isPlayerWinner(black: Int, white: Int): Int = when {
    white > black -> 0
    black > white -> 1
    black == white -> 2
    else -> -1
}

fun Game.resetScore():Game{
    return copy(score = (Color.entries + null).associateWith { 0 })
}

fun Game.isRunning(): Boolean = (board.getBoard().isNotEmpty())

fun Game.getTablePiece(c: Color): Int = board.getBoard().values.count { it.color == c }

// Place a piece and return a boolean value to tell if it was done successfully
fun Game.putPiece(pos: Position, newBoard: Table): Boolean {

    val tempPiece =
        Piece(position = pos, color = if (turnWhite) WHITE else BLACK)
    newBoard.mapBoard.putAll(this.board.mapBoard)

    if (!board.isValidPosition(pos, tempPiece)) return false

    // Check if this position is valid and get pieces to flip in one go
    val positionsToFlip = getPositionsToFlip(tempPiece)

    if (positionsToFlip.isNotEmpty()) {
        // Place the current player's piece
        newBoard.putPiece(tempPiece.position, tempPiece.color)

        // Change color of all positions that should be flipped
        for (position in positionsToFlip) {
            newBoard.changeColor(position, tempPiece.color)
        }
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
    validMovesFor(if (turnWhite) WHITE else BLACK)