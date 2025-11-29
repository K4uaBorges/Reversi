/**
 * @author Kauã Borges
 * @file Commands.kt
 *
 * This file defines all the available commands for the Reversi game console.
 * Each command is represented as an object that extends the abstract Command class.
 *
 * Commands include:
 *  @see PLAY -> Makes a move in a given position.
 *  @see NEW -> Starts a new local or named game.
 *  @see JOIN -> Joins an existing game.
 *  @see HELP -> Lists all available commands.
 *  @see PASS -> Passes the turn if no valid move exists.
 *  @see REFRESH -> Updates the game state in multiplayer mode.
 *  @see TARGETS -> Enables or disables the visual display of valid moves.
 *  @see SHOW -> Displays the current board.
 *  @see EXIT -> Exits the game safely.
 *
 * ---
 * This file acts as the **command center** of the application,
 * linking user input with game logic in an organized and extensible way.
 */

package ui.terminal

import model.*
import model.piece.Color
import model.piece.ColorEnum
import model.piece.ColorEnum.BLACK
import model.piece.ColorEnum.WHITE
import model.piece.Piece
import model.piece.Position
import model.table.TOTAL_BOARD_SIZE
import model.table.Table
import kotlin.system.exitProcess

abstract class Command(val commandHelpMsg: String) {
    open fun execute(args: List<String>, game: Game?): Game? = game
}


object Play: Command(commandHelpMsg = "PLAY <PositionCell(NUMBER,CHARACTER)> - plays the game in the position. ex: PLAY 4B") {
    override fun execute(args: List<String>, game: Game?): Game {

        if(game == null) throw Error("Game is not initialized")

        if(args.isEmpty()) { throw Error("INVALID FORMAT"); return game}

        val posInput = args[0].trim().uppercase()

        val row = posInput.getOrNull(0)?.digitToIntOrNull()
        val col = posInput.getOrNull(1)

        if (row == null || col == null || !col.isLetter()) {
            throw Error("INVALID FORMAT. Use like: PLAY 4C")
            return game
        }

        val pos = Position(row, col)

        // --- if the game is offline (turns) ---
        if (game.name == null) {
            game.play(pos)
            if (game.board.getMapBoard().size == TOTAL_BOARD_SIZE) game.finish()
            return game
        }

        // --- if the game is a multiplayer game (local game) ---
        game.play(pos)

            if (game.board.getMapBoard().size == TOTAL_BOARD_SIZE) game.finish()
        return game
    }
}

object New : Command(commandHelpMsg = "NEW (#|@) [<name>] - creates a new game. Example: Use: NEW # DANIEL") {
    override fun execute(args: List<String>, game: Game?): Game {
        var playerColor: Color? = null

        if(args.isEmpty()) throw Error("MISSING ARGUMENT-> Use: NEW (#|@) [<name>]")

        val input = args.joinToString(" ").trim()

        val regex = Regex("""^([#@])\s*(\w*)$""")
        val match = regex.matchEntire(input) ?:
            throw Error("INVALID FORMAT -> Use: NEW (#|@) [<name>]")

        val symbol = match.groupValues[1].first() // '#' ou '@'
        val name = match.groupValues[2].ifBlank { null } // <- if blank, equal null

        when (symbol) {
            '#' -> playerColor = Color(BLACK)
            '@' -> playerColor = Color(ColorEnum.WHITE)
            else -> println("Invalid player symbol. Use # for Black or @ for White")
        }

        val game = Game(name, true, Piece(Position(3,'A'),Color(BLACK)), Table(), playerColor!!)

        game.new()
        println("You are a player ${if (playerColor.color == BLACK) "#" else "@" }" +
                if (name != null) " in game $name." else ".")

        return game
    }
}

object Join : Command(commandHelpMsg = "JOIN <Player> - joins in game with another terminal. Example: JOIN KAUA") {
    override fun execute(args: List<String>, game: Game?): Game {
        if (args.isEmpty() || args.size != 1) throw Error("MISSING ARGUMENT -> Use: JOIN <name>")
        val input = args.joinToString(" ").trim()

        val local = Game(input, true, Piece(Position(3,'A'),Color(BLACK)), Table(), playerColor = Color(WHITE))  // a tua cor neste terminal
        val newLocalGame = local.load()
        println("Ligado ao jogo $input como ${if (newLocalGame.playerColor == Color(BLACK)) '#' else '@'}.")
        return newLocalGame
    }
}

object Help: Command(commandHelpMsg = "HELP - prints a list of commands"){
    override fun execute(args: List<String>, game: Game?): Game? {
        println("")
        getAllCommands().forEach { _, command -> println(command.commandHelpMsg) }
        return game
    }
}

object Pass: Command(commandHelpMsg = "PASS - Pass the turn, if you don't have another option to play") {
    override fun execute(args: List<String>, game: Game?): Game? {
        if (game == null) throw Error("Game not started")
        game.pass()
        return game
    }
}

object Refresh: Command(commandHelpMsg = "REFRESH - Refresh the game status, it command just work with you play in join game") {
    override fun execute(args: List<String>, game: Game?): Game {
        if (game == null) throw Error("Game not started")
        game.save()
        if(game.load() != game ) {
            game.load()
            game.show()
        } else throw Error("Nothing to refresh")

        return game
    }
}

object Targets: Command(commandHelpMsg = "TARGETS - Active or Desactive the vision of all Position you can play") {
    override fun execute(args: List<String>, game: Game?): Game {
        if (game == null) throw Error("Game not started")
        if (args.isEmpty()) throw Error("Use: TARGETS ON | TARGETS OFF")

        val opt = args.joinToString(" ").trim().lowercase()
        when (opt) {
            "on" -> {
                if (TARGETS_POSITIONS != null) throw Error("TARGETS já está ativado.")
                TARGETS_POSITIONS = emptySet()
                println("TARGETS ativado.")
            }
            "off" -> {
                if (TARGETS_POSITIONS == null) throw Error("TARGETS já está desativado.")
                TARGETS_POSITIONS = null
                println("TARGETS desativado.")
            }
            else -> throw Error("Use: TARGETS ON | TARGETS OFF")
        }
        return game
    }
}

object Show: Command(commandHelpMsg = "SHOW - Desactive the vision of all positions you can play") {
    override fun execute(args: List<String>, game: Game?): Game {
        if (game == null) throw Error("Game not started")
        game.show()
        return game
    }
}

object Exit: Command(commandHelpMsg = "EXIT - Close game, if you use a command join to play so its already saved") {
    override fun execute(args: List<String>, game: Game?): Game {
        println("Bye")
        exitProcess(0)
    }
}



fun getAllCommands(): Map<String, Command> = mapOf(
    "PLAY" to Play,
    "NEW" to New,
    "JOIN" to Join,
    "HELP" to Help,
    "PASS" to Pass,
    "REFRESH" to Refresh,
    "TARGETS" to Targets,
    "SHOW" to Show,
    "EXIT" to Exit
    )

