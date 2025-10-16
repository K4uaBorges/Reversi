package Game.Piece.Color
data class Color(var color: ColorEnum)

fun Color.setColor(color: ColorEnum) = color
fun Color.getColor(): ColorEnum = color
