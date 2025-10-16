//package Terminal
//
//fun Game.show( ){
//    board.chunked(BOARD_SIZE).forEachIndexed { rowIdx, rowList ->
//        println( " "+rowList.map{ p-> p?.toString()?:" "}.joinToString ( " | "))
//        if( rowIdx < BOARD_SIZE-1) println("---+---+---")
//    }
//    println( when{
//        isWinner(Player.X) -> "Winner X"
//        isWinner(Player.O) -> "Winner O"
//        isDraw() -> "Draw"
//        else -> "Turn: $turn"
//    })
//}