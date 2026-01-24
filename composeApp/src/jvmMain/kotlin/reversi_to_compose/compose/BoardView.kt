package reversi_to_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import reversi_to_compose.model.piece.Position
import reversi_to_compose.model.table.BOARD_SIZE
import reversi_to_compose.model.table.Table
import reversi_to_compose.view_model.TARGETS_SET

const val BALL_SIZE = 0.35f
val CELL_SIZE = 75.dp
val LINE_THICKNESS = 2.dp
val GRID_SIZE = CELL_SIZE * BOARD_SIZE + LINE_THICKNESS * (BOARD_SIZE - 1)

val charToInt: (Char) -> Int = { (it.code - 'A'.code) }
val intToChar: (Int) -> Char = { (it + 'A'.code).toChar() }
@Composable
fun BoardView(board: Table, onCellClickAction: (Position) -> Unit) {

    Column(
        modifier = Modifier.height(GRID_SIZE).background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(BOARD_SIZE) { row ->
            Row(
                modifier = Modifier.width(GRID_SIZE).background(Color.Black),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(BOARD_SIZE) { col ->
                    val position = Position(row + 1, intToChar(col))
                    val isTarget = TARGETS_SET.contains(position)

                    Box(
                        modifier = Modifier
                            .size(CELL_SIZE)
                            .background(Color.Green)
                    ) {
                        PlayerView(
                            piece = board.getPiece(position)?.color,
                            onClick = { onCellClickAction(position) },
                            modifier = Modifier.fillMaxSize()
                        )

                        if (isTarget) {
                            Box(
                                modifier = Modifier
                                    .size(CELL_SIZE * BALL_SIZE)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(Color.Yellow)
                            )
                        }
                    }
                }
            }
        }
    }
}
