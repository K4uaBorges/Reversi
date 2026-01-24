package reversi_to_compose.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import reversi_to_compose.view_model.APPViewModel


@Composable
@Preview
fun FrameWindowScope.ReversiApp(onExit: () -> Unit) {


    MaterialTheme {

        val vm = remember { APPViewModel() }

        MenuBar {
            Menu("Game") {
                Item("New Game", onClick = { vm.showNewStartDialog() })
                Item("Join", onClick = { vm.runSafely { vm.showJoinStartDialog() } })
                Item("Refresh", onClick = { vm.requestRefresh() }, enabled = vm.isMultiplayer && vm.canUsed)
                Item("Exit", onClick = onExit)
            }
            Menu("Options") {
                CheckboxItem(
                    text = "Show Targets",
                    checked = vm.isTargetOn,
                    onCheckedChange = { vm.isTargetOn = it },
                    enabled = vm.canUsed,
                )

                CheckboxItem(
                    text = "Auto-Refresh",
                    checked = vm.isAutoRefresh,
                    onCheckedChange = { vm.isAutoRefresh = it },
                    enabled = vm.canUsed && vm.isMultiplayer
                )

            }
            Menu("Play") {
                Item(
                    text = "Pass",
                    onClick = { vm.runSafely { vm.pass() } },
                    enabled = vm.canUsed && vm.isEnablePass()
                )
                Item(
                    text = "Score",
                    onClick = { vm.showScore() },
                    enabled = vm.canUsed && vm.isRun
                )
                Item(
                    text = "Reset Score",
                    onClick = { vm.restartScore() },
                    enabled = vm.canUsed && vm.isRun
                )
            }
        }

        Column {
            if (vm.isRun) {
                BoardView(vm.game!!.board, vm::play)
                StatusBarView(vm.game!!)
            } else {
                Box(Modifier.size(GRID_SIZE, GRID_SIZE + STATUS_HEIGHT))
            }
            if (vm.isShowScoreDial) {
                ScoreDialog(vm.game!!.score, vm::hideScore)
            }
            LaunchedEffect(vm.refreshTick) {
                if (vm.isMultiplayer) {
                    vm.runSafely { vm.refresh() }
                }
            }
            LaunchedEffect(vm.isAutoRefresh, vm.isMultiplayer) {
                if (vm.isAutoRefresh && vm.isMultiplayer) {
                    vm.runSafely { vm.startAutoRefresh( 100L ) }
                } else {
                    vm.stopAutoRefresh()
                }
            }
            LaunchedEffect(vm.isFullBoard, vm.getPass(), vm.isRun) {
                vm.isFinish = (vm.isRun && (vm.isFullBoard || vm.getPass() > 1))
            }
            LaunchedEffect(vm.isTargetOn, vm.game) {
                vm.target()
            }
        }

        vm.newOrJoin?.let {
            if (vm.newOrJoin == NewOrJoinType.NewGameEnum) {
                newGameJoinDialog(it, vm::hideNewOrJoinDialog, vm::NewOrJoinGame)
            } else {
                joinDialog(vm::hideNewOrJoinDialog, vm::JoinGame)
            }
        }

        vm.errorMessage?.let { msg -> ErrorDialog(msg, vm::hideError) }

        if(vm.isFinish){
            finishDialg({ vm.hideFinishDialog() }, onExit, { vm.runSafely { vm.restart() } })
        }
        LaunchedEffect(vm.isFinish) {
            vm.finish()
            delay(500)
            java.awt.Toolkit.getDefaultToolkit().beep()
        }
    }
}

