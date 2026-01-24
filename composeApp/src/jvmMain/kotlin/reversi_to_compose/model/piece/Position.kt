package reversi_to_compose.model.piece

enum class Directions(val dRow: Int, val dCol: Int) {
    LEFT(0, -1),
    RIGHT(0, 1),
    UP(-1, 0),
    DOWN(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

}

data class Position(val row:Int, val col: Char){

    override fun toString(): String {
        return "$row$col"
    }

    operator fun plus(dir: Directions): Position {
        return Position(
            row + dir.dRow,
            (col.code + dir.dCol).toChar()
        )
    }
}