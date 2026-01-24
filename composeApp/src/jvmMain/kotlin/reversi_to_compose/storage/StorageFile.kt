package reversi_to_compose.storage


import reversi_to_compose.model.Game
import reversi_to_compose.model.*
import reversi_to_compose.model.piece.*
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.table.Table
import java.nio.file.Path
import kotlin.io.path.*

private const val BLACK_NUMBER = 0
private const val WHITE_NUMBER = 1

class StorageFile(name: String?) {

    val path = Path("ReversiGameData")
    val file: Path = path.resolve("$name.txt")

    // Save file and write
    fun save(game: Game) {
        if (!path.exists()) path.createDirectory()
        if (!file.exists()) file.createFile()

        val builder = StringBuilder()

        builder.append("turnWhite=${if (game.turnWhite) WHITE_NUMBER else BLACK_NUMBER}\n")
        builder.append(
            "playerColor=${
                when (game.playerColor) {
                    BLACK -> BLACK_NUMBER
                    WHITE -> WHITE_NUMBER
                }
            }\n"
        )

        game.board.getBoard().forEach { (pos, piece) ->
            val colorValue = if (piece.color == BLACK) BLACK_NUMBER else WHITE_NUMBER
            builder.append("${pos.row}${pos.col}:$colorValue\n")
        }

        file.writeText(builder.toString())
    }

    // Load file and read
    fun load(game: Game): Game {

        if (!file.exists())
            throw NoStorageOrModifierFileException()

        val loadedBoard = Table(HashMap()) // board limpa
        val rawLines = file.readLines()
        if (rawLines.isEmpty())
            throw NoStorageOrModifierFileException()

        // Skip empty lines
        val lines = rawLines.map { it.trim() }.filter { it.isNotEmpty() }

        // Line 0: turnWhite (0 = Black | 1 = White)
        var turnLine = lines.first()
        val turnWhiteInt = turnLine.substringAfter("turnWhite=").toIntOrNull()
            ?: throw NoStorageOrModifierFileException()

        // Line 1: playerColor (0 = White | 1 = Black)
        turnLine = lines[1]
        val playerColorInt = turnLine.substringAfter("playerColor=").toIntOrNull()
            ?: throw NoStorageOrModifierFileException()

        // Read Piece
        for (line in lines.drop(2)) {
            val parts = line.split(":")
            if (parts.size != 2) continue

            val positionStr = parts[0].trim()
            val colorChar = parts[1].trim().firstOrNull() ?: continue

            if (positionStr.length < 2) continue

            val rowStr = positionStr.dropLast(1)
            val colChar = positionStr.last()

            val row = rowStr.toIntOrNull() ?: continue
            val color = when (colorChar) {
                '0' -> BLACK
                '1' -> WHITE
                else -> continue
            }

            loadedBoard.putPiece(Position(row, colChar), color)
        }

        return game.copy(
            turnWhite = when (turnWhiteInt) {
                BLACK_NUMBER -> false
                WHITE_NUMBER -> true
                else -> throw NoStorageOrModifierFileException()
            },
            playerColor = when (playerColorInt) {
                BLACK_NUMBER -> BLACK
                WHITE_NUMBER -> WHITE
                else -> throw NoStorageOrModifierFileException()
            },
            board = loadedBoard
        )
    }
}