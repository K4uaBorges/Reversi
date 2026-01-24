package reversi_to_compose.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import reversi_to_compose.model.Score
import reversi_to_compose.model.piece.Color

@Composable
fun BaseInfoDialog(title: String, closeAction: () -> Unit, content: @Composable () -> Unit) = AlertDialog(
    onDismissRequest = closeAction,
    title = { Text(title) },
    text = content,
    confirmButton = { TextButton(closeAction) { Text("Close") } }
)

@Composable
fun ScoreDialog(score: Score, closeAction: () -> Unit) = BaseInfoDialog(
    title = "Score",
    closeAction = closeAction,
    content = { ScoreDialogContent(score) }
)

@Composable
fun ScoreDialogContent(score: Score) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Column {
            Color.entries.forEach { color ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlayerView(
                        piece = color,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(text = " - ${score[color]}")
                }
            }
        }
        Text(text = "Draws - ${score[null]}")
    }
}