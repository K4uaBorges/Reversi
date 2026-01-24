package reversi_to_compose.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import reversi_to_compose.model.piece.Color.*
import org.jetbrains.compose.resources.*
import reversi_compose.composeapp.generated.resources.*
import reversi_to_compose.model.piece.Color

fun resourse(color: Color): DrawableResource {
    return when (color) {
        WHITE -> Res.drawable.white
        BLACK -> Res.drawable.black
    }
}

@Composable
@Preview
fun PlayerView(
    piece: Color?,
    modifier: Modifier = Modifier.Companion.size(CELL_SIZE),
    onClick: () -> Unit = {}
) {

    if (piece == null) {
        Box(modifier.clickable(onClick = onClick))
    } else {
        Image(
            painter = painterResource(resourse(piece)),
            contentDescription = null,
            modifier = modifier
        )
    }
}



