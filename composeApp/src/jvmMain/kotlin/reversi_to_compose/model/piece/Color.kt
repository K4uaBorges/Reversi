package reversi_to_compose.model.piece

import reversi_to_compose.model.piece.Color.*

enum class Color {
    WHITE,
    BLACK,
}

operator fun Color.not() : Color {
    val newColor : Color = when (this) {
        BLACK -> WHITE
        WHITE -> BLACK
    }
    return newColor
}