package reversi_to_compose.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import reversi_to_compose.compose.NewOrJoinType.*
import reversi_to_compose.compose.*
import reversi_to_compose.model.*
import reversi_to_compose.model.piece.Color
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.piece.Position
import reversi_to_compose.model.table.TOTAL_BOARD_SIZE
import reversi_to_compose.model.table.Table
import java.util.concurrent.Semaphore


var TARGETS_SET by mutableStateOf<Set<Position>>(emptySet())

class APPViewModel() {

    var clash by mutableStateOf(
        Clash(
            Game(
                name = null,
                turnWhite = true,
                board = Table(HashMap(0)),
                playerColor = BLACK,
                score = (Color.entries + null).associateWith { 0 }
            ))
    )

    private val moveSignal = Semaphore(0)
    private var autoJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    var errorMessage: String? by mutableStateOf(null)
        private set
    var newOrJoin: NewOrJoinType? by mutableStateOf(null)
        private set

    var isTargetOn: Boolean by mutableStateOf(false)
    var isAutoRefresh: Boolean by mutableStateOf(false)
    var isFinish: Boolean by mutableStateOf(false)

    val game: Game? get() = clash.game

    val whitePieceInTable: Int get() = game!!.getTablePiece(WHITE)

    val blackPieceInTable: Int get() = game!!.getTablePiece(BLACK)

    val isRun: Boolean get() = clash.game != null

    var isShowScoreDial: Boolean by mutableStateOf(false)
        private set
    var isMultiplayer: Boolean = false
    val isFullBoard: Boolean get() = clash.getBoard().mapBoard.size == TOTAL_BOARD_SIZE

    val canUsed: Boolean get() = clash.game!!.board.mapBoard.isNotEmpty()
    var refreshTick by mutableStateOf(0)
        private set

    var finalTick by mutableStateOf(0)
        private set

    fun requestRefresh() {
        refreshTick++
    }

    inline fun runSafely(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            setError(e)
        }
    }

    fun gameHealthy() {
        check(canUsed){ NoGameStartedException()}
        check(isRun){{ NoGameStartedException()}}
    }

    fun NewOrJoinGame(name: String?, c: Color) {
        newGame(name, c)
        hideNewOrJoinDialog()
    }

    fun JoinGame(name: String) {
        joinGame(name)
        hideNewOrJoinDialog()
    }

    fun newGame(name: String?, playerColor: Color) {
        runSafely {  clash = clash.new(game!!, name, playerColor) }
        if (name != null) {
            isMultiplayer = true
        }
    }

    fun restartScore(){
        clash = clash.resetScore()
    }


    fun startAutoRefresh(intervalMs: Long = 2000L) {
        if (autoJob != null) return
        autoJob = scope.launch {
            while (isActive) {
                runSafely { refresh() }   // vai buscar estado novo (server/db)
                requestRefresh()          // força recomposição se precisares
                delay(intervalMs)
            }
        }
    }

    fun stopAutoRefresh() {
        autoJob?.cancel()
        autoJob = null
    }
    fun notifyMoveHappened() {
        if (moveSignal.availablePermits() == 0) {
            moveSignal.release()
        }
    }

    fun joinGame(name: String) {
        runSafely { clash = clash.join(name)}
        isMultiplayer = true
    }

    fun pass() {
        runSafely { clash = clash.pass() }
        notifyMoveHappened()
    }

    fun refresh() {
        clash = clash.refresh(isAutoRefresh)
    }

    fun play(pos: Position) {
        runSafely {gameHealthy()}
        clash = clash.play(pos)
        notifyMoveHappened()
    }

    fun finish() {
        if (isRun && (game!!.board.mapBoard.size == TOTAL_BOARD_SIZE || getPassCount() > 1) ) {
           clash = clash.finish(blackPieceInTable, whitePieceInTable)
            isFinish = true
            finalTick++
            resetPassCount()
        }
    }

    fun target() {
        TARGETS_SET = clash.target(isTargetOn)
    }

    fun restart() {
        finalTick = 0
        refreshTick = 0
        clash = clash.restart()
        notifyMoveHappened()
    }

    fun setError(e: Exception) {
        errorMessage = e.message
    }

    fun isEnablePass(): Boolean = clash.target(true).isEmpty()
    fun showNewStartDialog() {
        newOrJoin = newOrJoin.let { NewGameEnum }
    }

    fun showJoinStartDialog() {
        newOrJoin = newOrJoin.let { JoinEnum }
    }

    fun hideFinishDialog() {
        isFinish = false
    }

    fun hideNewOrJoinDialog() {
        newOrJoin = null
    }

    fun hideScore() {
        isShowScoreDial = false
    }

    fun showScore() {
        isShowScoreDial = true
    }

    fun hideError() {
        errorMessage = null
    }

    fun getPass():Int {
        return getPassCount()
    }
}