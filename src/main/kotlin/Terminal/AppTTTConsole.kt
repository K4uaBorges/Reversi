package Terminal
//
//class AppConsoleReversi {
//    fun run() {
//        var game: Game? = null
//        val commands: Map<String, Command> = getAllCommands()
//        while (true) {
//            print("$ ")
//            val (cmd, args) = readCommand()
//            val command = commands[cmd]
//            if (command != null) {
//                game = command.execute(args, game)
//            } else {
//                println("Invalid command $cmd")
//            }
//            if (game == null) {
//                println("create a game to start playing")
//            } else {
//                game.show()
//            }
//        }
//    }
//}
