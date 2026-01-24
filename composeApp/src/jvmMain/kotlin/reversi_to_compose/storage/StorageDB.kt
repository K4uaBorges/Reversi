package reversi_to_compose.storage

import reversi_to_compose.model.Game
import reversi_to_compose.model.MongoClientException
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.piece.Position
import reversi_to_compose.model.table.Table
import reversi_to_compose.storage.mongoDB.Serializer
import java.util.HashMap

private const val BLACK_NUMBER = 0
private const val WHITE_NUMBER = 1

object StorageDB : Serializer<Game> {

    override fun serialize(d: Game): String {
        val sb = StringBuilder()

        sb.append("turnWhite=${if (d.turnWhite) WHITE_NUMBER else BLACK_NUMBER}\n")
        sb.append("playerColor=${if (d.playerColor == BLACK) BLACK_NUMBER else WHITE_NUMBER}\n")

        d.board.getBoard().forEach{ (pos, piece) ->
            val colorValue = if(piece.color == BLACK) BLACK_NUMBER else WHITE_NUMBER
            sb.append("${pos.row}${pos.col}:$colorValue\n")
        }

        return sb.toString()
    }

    override fun deserialize(txt: String): Game {
        val lines = txt
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()

        require(lines.size >= 2) { MongoClientException("Dados inválidos no Mongo (faltam linhas base)") }

        val turnWhiteInt = lines[0].substringAfter("turnWhite=").toInt()
        val playerColorInt = lines[1].substringAfter("playerColor=").toInt()

        val loadedBoard = Table(HashMap())

        for (line in lines.drop(2)) {
            val parts = line.split(":")
            if (parts.size != 2) continue

            val positionStr = parts[0].trim()
            val colorStr = parts[1].trim()

            if (positionStr.length < 2) continue

            val rowStr = positionStr.dropLast(1)
            val colChar = positionStr.last()

            val row = rowStr.toIntOrNull() ?: continue

            val color = when (colorStr.firstOrNull()) {
                '0' -> BLACK
                '1' -> WHITE
                else -> continue
            }

            loadedBoard.putPiece(Position(row, colChar), color)
        }

        // Nota: aqui não sabemos "name" nem "score" — isso vem do Game que chama loadDB()
        // Por isso devolvemos um Game "parcial" e depois tu fazes copy no loadDB.
        return Game(
            name = null,
            turnWhite = (turnWhiteInt == WHITE_NUMBER),
            board = loadedBoard,
            playerColor = if (playerColorInt == BLACK_NUMBER) BLACK else WHITE
        )
    }
}
