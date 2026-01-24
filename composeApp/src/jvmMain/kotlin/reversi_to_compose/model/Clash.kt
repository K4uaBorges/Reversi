package reversi_to_compose.model


import reversi_to_compose.model.piece.Color
import reversi_to_compose.model.piece.Position
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.piece.Piece
import reversi_to_compose.model.piece.not
import reversi_to_compose.model.table.Table
import java.util.HashMap

open class Clash(val game: Game?)
class ClashRun(game: Game?) : Clash(game)

private var TARGETS_POSITION: Set<Position> = emptySet()
private var passCount = 0


fun Clash.restart(): ClashRun {
    checkNotNull(game){throw NoGameStartedException()}

    val newGame = game.new()
    return ClashRun(newGame)
}

fun Clash.getBoard(): Table = game!!.board
fun Clash.play(pos: Position): ClashRun{
    checkNotNull(game){throw NoGameStartedException()}
    checkNotNull(game.playerColor){throw NoPlayerColorException()}

    val newGame = game.play(pos)
    if(newGame.isValid()
        && newGame.board.getBoard().size != game.board.getBoard().size){
        newGame.saveDB()
        //saveFile()
    }
    resetPassCount()
    return ClashRun(newGame)
}

fun Clash.new(currentGame: Game, name: String? , chooseColor: Color):ClashRun {

    var newGame = currentGame.copy(name = name, playerColor = chooseColor)
    newGame = newGame.new()

    if(newGame.isValid()){
        newGame.saveDB()
        //saveFile()
    }

    return ClashRun(newGame)
}

fun Clash.join(name: String): ClashRun{
    var newGame = Game(
        name = name,
        turnWhite = true,
        board = Table(HashMap<Position, Piece>(0)),
        playerColor = BLACK,
        score = (Color.entries + null).associateWith { 0 }
    )
    newGame = newGame.loadDB()
    newGame = newGame.copy(playerColor = newGame.playerColor.not())
    return ClashRun(newGame)
}

fun Clash.refresh(isAutoRefresh :Boolean): ClashRun {
    checkNotNull(game){NoGameStartedException()}
    checkNotNull(game.playerColor){NoPlayerColorException()}

    var readGame = game.loadDB()
    readGame = readGame.copy(playerColor = game.playerColor)

    if(readGame.board.getBoard().size == game.board.getBoard().size && !isAutoRefresh)
        throw NoChangesException()

    return ClashRun(readGame)
}

fun Clash.pass(): ClashRun{
    checkNotNull(game){throw NoGameStartedException()}
    val newGame = game.pass()
    passCount++

    return ClashRun(newGame)
}

fun Clash.finish(blackCount:Int, whiteCount:Int): ClashRun{

    checkNotNull(game){throw NoGameStartedException()}
    val newGame = game.finish(blackCount,whiteCount)

    return ClashRun(newGame)
}


fun Clash.target(opt: Boolean): Set<Position>{
    checkNotNull(game){throw NoGameStartedException() }
    checkNotNull(game.playerColor){NoPlayerColorException()}

    if (opt) { TARGETS_POSITION = TARGETS_POSITION.let { game.validMoves().toSet() } }
    else {TARGETS_POSITION = emptySet()}

    return TARGETS_POSITION
}

fun resetPassCount(){
    passCount = 0
}

fun Clash.resetScore(): ClashRun{
    checkNotNull(game){throw NoGameStartedException()}
    val newGame = game.resetScore()

    return ClashRun(newGame)
}
fun getPassCount(): Int {
    return passCount
}