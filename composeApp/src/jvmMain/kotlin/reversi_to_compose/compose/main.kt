package reversi_to_compose.compose

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import org.jetbrains.compose.resources.painterResource
import reversi_to_compose.model.piece.Color

fun main() = application {
    Window(
        state = WindowState(size = DpSize.Unspecified),
        onCloseRequest = ::exitApplication,
        resizable = false,
        title = "Reversi Compose App",
        icon = painterResource(resourse(Color.BLACK))
    ) {
        ReversiApp(::exitApplication)
    }
}