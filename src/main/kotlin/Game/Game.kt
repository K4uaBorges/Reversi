/*package Game
import Game.Piece.Color.Color
import Game.Piece.Position.*
import Game.Pieces.Piece
import Game.Table.*

const val BOARD_TOTAL_CELLS = 16
val EMPTY = null


data class Game(
    var Player: Piece,
    val Board: HashMap<Position, Piece> = Table().mapBoard,
)

fun Game.putPiece(pos: Position) {
    val searchPiece = Search()
    if (Table().isValidPosition(pos,Player)){
        val search = searchPiece.searchInDiag(Player,true)

            return true
        }
    }
}

fun Game.play(pos: Position): Game {
    TODO("not implemented")
}

fun Game.new(): Game{
    TODO("not implemented")
}

fun Game.canPLay(pos: Position) {
    TODO("not implemented")
}

fun Game.isWinner(){
    TODO("not implemented")
}
/** Assuming isWinner is called before.
 *And isDraw is never called when we already have a winner
 */
fun Game.isDraw() {
    TODO("not implemented")
}
*/