package Game.Pieces.Position.Positions

enum class Directions(val dRow: Int, val dCol: Int) {
    LEFT(0, -1),
    RIGHT(0, 1),
    UP(-1, 0),
    DOWN(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    fun getRowDelta(): Int = dRow
    fun getColDelta(): Int = dCol
}
