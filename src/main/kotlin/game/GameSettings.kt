/**
 * @author Kauã Borges
 * @file GameSettings.kt
 *
 * This file contains the GameSettings class, responsible for managing
 * game persistence — including creating, saving, and loading game files.
 *
 * It ensures that players can store and resume their games seamlessly,
 * handle all file operations and data management required for that process,
 * and create a game in another terminal
 */

package game

import game.piece.color.Color
import game.piece.color.ColorEnum.*
import game.piece.position.Position
import java.nio.file.Path
import kotlin.io.path.*

class GameSettings(name: String? = null) {

    val path = Path("ReversiGameData")
    val file: Path = path.resolve("$name.txt")

    // Save file and write
    fun save(game: Game) {
        if(!path.exists()) path.createDirectory()
        if(!file.exists()) file.createFile()

        val builder = StringBuilder()

        builder.append("turnWhite=${if (game.turnWhite) 0 else 1}\n")
        builder.append(
            "playerColor=${
                when (game.playerColor?.color) {
                    WHITE -> 0
                    BLACK -> 1
                    else -> 0 // If there isn't null
                }
            }\n"
        )

        game.board.getMapBoard().forEach { (pos, piece) ->
            val colorValue = if (piece.color == Color(WHITE)) 0 else 1
            builder.append("${pos.row}${pos.col}:$colorValue\n")
        }

        file.writeText(builder.toString())
    }

    // Load file and read
    fun load(game: Game): Game {

        if(!file.exists())
            throw Error("File does not exist, probably is corrupted or deleted")

        val newGame:Game
        val rawLines = file.readLines()
        if (rawLines.isEmpty())
            throw Error("Ficheiro inválido ou vazio.")

        // Skip empty lines
        val lines = rawLines.map { it.trim() }.filter { it.isNotEmpty() }

        // Line 0: turnWhite (0 = Black | 1 = White)
        var turnLine = lines.first()
        val turnWhiteInt = turnLine.substringAfter("turnWhite=").toIntOrNull()
            ?: throw Error("Invalid turnWhite value.")
        game.turnWhite = when (turnWhiteInt) {
            0 -> true
            1 -> false
            else -> throw Error("Invalid turnWhite value.")
        }

        // Line 1: playerColor (0 = White | 1 = Black)
        turnLine= lines[1]
        val playerColorInt = turnLine.substringAfter("playerColor=").toIntOrNull()
            ?: throw Error("Invalid PlayerColor value.")

        // Using a command JOIN entry in game, inverting color
        newGame = when (playerColorInt) {
        // Init Player WHITE
            0 -> Game(name = game.name,
                turnWhite = game.turnWhite,
                currentPlayer = game.currentPlayer,
                board = game.board,
                playerColor = Color(BLACK)
            )
        // Init Player BLACK
            1 -> Game(name = game.name,
                turnWhite = game.turnWhite,
                currentPlayer = game.currentPlayer,
                board = game.board,
                playerColor = Color(WHITE)
            )

            else -> throw Error("Invalid PlayerColor value.")
        }
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
                '0' -> Color(WHITE)
                '1' -> Color(BLACK)
                else -> continue
            }

            newGame.board.putPiece(Position(row, colChar), color)
        }

        return newGame
    }
}

