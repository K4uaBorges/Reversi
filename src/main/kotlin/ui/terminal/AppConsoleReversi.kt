/**
 * @author Kauã Borges
 * @file AppConsoleReversi.kt
 *
 * This file defines the AppConsoleReversi class, responsible for running
 * the console version of the Reversi game.
 *
 * It initializes the game loop, handles user input, interprets commands,
 * manages game errors, and continuously updates the board state.
 *
 * The class interacts with all other core modules (Game, Commands, Output)
 * to provide a smooth and responsive console experience for the player.
 *
 * ---
 * This file serves as the **entry point and control center** of the entire
 * game application, coordinating commands, logic, and user interaction.
 */


package ui.terminal

import model.Game
import java.io.FileNotFoundException
import java.io.IOException


class AppConsoleReversi {
    fun run() {
        var game: Game? = null
        val commands: Map<String, Command> = getAllCommands()
        
        println("Welcome to Reversi Game!")
        println("Type 'HELP' to see available commands.")
        println("Type 'NEW (#|@) [name]' or 'JOIN [name]' to start a new game.")
        println()
        
        while (true) {
            try {
                val (cmd, args) = readCommand()
                val command = commands[cmd]
                
                if (command != null) {
                    game = command.execute(args, game)
                } else {
                    println("Invalid command: $cmd")
                    println("Type 'HELP' to see available commands.")
                }
                
                // Show game state after each command
                if (game == null) {
                    println("Create a game to start playing. Use: NEW # [name] or NEW @ [name]")
                } else {
                    game.show()
                }
                
            } catch (e: Error) {
                // Handle specific game errors
                when {
                    e.message?.contains("INVALID POSITION") == true -> {
                        println("${e.message}")
                    }
                    e.message?.contains("INVALID FORMAT. Use like: PLAY 4B") == true -> {
                        println("${e.message}")
                    }
                    e.message?.contains("Invalid row number") == true -> {
                        println("INVALID FORMAT, Number out of range Row")
                    }
                    e.message?.contains("Invalid col number") == true -> {
                        println("INVALID FORMAT, Number out of range col")
                    }
                    e.message?.contains("Game is not initialized") == true -> {
                        println("Game not initialized")
                        println(("Type 'NEW (#|@) [name]' or 'JOIN [name]' to start a new game."))
                    }
                    e.message?.contains("Game not started") == true -> {
                        println("Game not started")
                        println(("Type 'NEW (#|@) [name]' or 'JOIN [name]' to start a new game."))
                    }
                    e.message?.contains("Cannot save game") == true -> {
                        println("Cannot save game: ${e.message}")
                        println("This is a local game. Use NEW # [name] or NEW @ [name] to create a saveable game.")
                    }
                    e.message?.contains("You can't Play, is not your turn") == true -> {
                        println("Cannot save game: ${e.message}")
                        println("This is a local game. Use NEW # [name] or NEW @ [name] to create a saveable game.")
                    }
                    e.message?.contains("Cannot load game") == true -> {
                        println("Cannot load game: ${e.message}")
                        println("This is a local game. Use JOIN [name] to join an existing game.")
                    }
                    e.message?.contains("YOU CAN'T PLAY, IT'S NOT YOUR TURN") == true -> {
                        println("${e.message}")
                    }
                    e.message?.contains("It's not your turn") == true -> {
                        println("${e.message}")
                        println("Wait for your turn or use REFRESH to check for updates.")
                    }
                    e.message?.contains("Nothing to refresh") == true -> {
                        println("${e.message}")
                    }
                    e.message?.contains("Error in Counting piece") == true -> {
                        println("${e.message}")
                    }
                    e.message?.contains("File does not exist") == true -> {
                        println("File error: ${e.message}")
                        println("The game file may be corrupted or deleted. Try creating a new game.")
                    }
                    e.message?.contains("Invalid turnWhite value") == true -> {
                        println("Game file corrupted: ${e.message}")
                        println("The game file is corrupted. Try creating a new game.")
                    }
                    e.message?.contains("The game file is corrupted. Try creating a new game.") == true -> {
                        println("Invalid file: ${e.message}")
                        println("The game file is corrupted. Try creating a new game.")
                    }
                    e.message?.contains("MISSING ARGUMENT") == true -> {
                        println("${e.message}")
                        println("Check the command format. Use HELP to see examples.")
                    }
                    e.message?.contains("INVALID FORMAT") == true -> {
                        println("${e.message}")
                        println("Check the command format. Use HELP to see examples.")
                    }
                    e.message?.contains("Failed to load game") == true -> {
                        println("${e.message}")
                        println("Make sure the game exists and you have permission to access it.")
                    }
                    e.message?.contains("Not implemented yet") == true -> {
                        println("${e.message}")
                    }
                    else -> {
                        println("Game Error: ${e.message}")
                        println("Use HELP to see available commands or create a new game.")
                    }
                }
            } catch (e: FileNotFoundException) {
                println("File not found: ${e.message}")
                println("The game file may have been deleted. Try creating a new game.")
            } catch (e: IOException) {
                println("File access error: ${e.message}")
                println("Check file permissions or try creating a new game.")
            } catch (e: NumberFormatException) {
                println("Invalid number format: ${e.message}")
                println("Check your input format. Use HELP to see examples.")
            } catch (e: IllegalArgumentException) {
                println("Invalid argument: ${e.message}")
                println("Check your command arguments. Use HELP to see examples.")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
                println("Please try again or restart the application.")
                e.printStackTrace()
            }
        }
    }
}
