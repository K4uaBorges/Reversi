package reversi_to_compose.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import reversi_to_compose.model.Game
import reversi_to_compose.model.getTablePiece
import reversi_to_compose.model.isRunning
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.piece.Piece

val STATUS_HEIGHT = 50.dp

@Composable
private fun Disk(c: reversi_to_compose.model.piece.Color, size: Int = 18) {
    val fill = if (c == WHITE) Color.White else Color.Black
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(fill)
    )
}

@Composable
private fun StatusChip(
    label: String,
    diskColor: reversi_to_compose.model.piece.Color? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFFD9D9D9))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        if (diskColor != null) {
            Spacer(Modifier.width(8.dp))
            Disk(diskColor)
        }
    }
}

@Composable
fun StatusBarView(game: Game) {
    Row(
        modifier = Modifier
            .background(Color.LightGray)
            .width(GRID_SIZE)
            .height(STATUS_HEIGHT)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
    ) {
        if (!game.isRunning()) {
            StatusChip("Start a New Game")
            return
        }

        // Turn (cor da peça que joga agora)
        val turnColor = if (game.turnWhite) WHITE else BLACK
        StatusChip("Turn", diskColor = turnColor)

        // You (se tiveres isto no Game: playerColor / ou algo equivalente)
        // Se não tiveres, diz-me o nome exato e eu ajusto.
        val youColor = game.playerColor   // <- usa o teu campo real
        StatusChip("You", diskColor = youColor)

        // Contagens
        val whiteCount = game.getTablePiece(WHITE)
        val blackCount = game.getTablePiece(BLACK)

        StatusChip("${whiteCount}x", diskColor = WHITE)
        StatusChip("${blackCount}x", diskColor = BLACK)
    }
}
