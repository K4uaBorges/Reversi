# Reversi Game

A simple implementation of the classic board game *Reversi* (also known as *Othello*) in Kotlin. This project simulates the gameplay for two players, where each player alternates turns placing pieces on an 8x8 grid, aiming to "flip" the opponent's pieces and control more of the board by the end of the game.

## Features

- **8x8 Game Board**: The game uses a standard 8x8 board with two colors of pieces (black and white).
- **Turn-Based Gameplay**: Players take turns placing their pieces on the board.
- **Move Validation**: Moves are validated for correctness, ensuring pieces can only be placed in valid positions where they can flip the opponent's pieces.
- **Victory Condition**: The game ends when there are no more valid moves for either player. The player with the most pieces on the board at the end wins.
- **Game Flow Management**: Includes features to start a new game, check for victory, save/load game progress.

## Classes

- **Game**: Manages the game logic, such as player turns, move validation, and checking for victory conditions.
- **Piece**: Represents a single piece on the board, with color attributes (black or white).
- **Table**: The 8x8 board where the game takes place. It stores the state of the game and provides functionality to place pieces and check for valid moves.
- **Color**: Enum class that represents the two colors of pieces (Black and White).
- **Direction**: Enum class representing the eight possible directions for flipping opponent's pieces (horizontal, vertical, and diagonal).

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/reversi-game.git

2. Navigate to the project directory:
   ```bash
   cd reversi-game

3. Open the project in your preferred IDE (e.g., IntelliJ IDEA, VS Code) or
  ```bash
  # Run the game
  kotlinc Game.kt -include-runtime -d Reversi.jar
  java -jar Reversi.jar
  ```

4. Run the project using Kotlin.

Usage

- Upon starting the game, the board will be displayed, and players will take turns to place their pieces.

- Each valid move will flip one or more of the opponent's pieces.

- The game will announce the winner when all possible moves have been exhausted.

# Example of Game Flow
Here’s an example of how the game might look when executed:

```bash
You are a player @.
   A B C D E F G H
1  . . . . . . . .
2  . . . . . . . .
3  . . . . . . . .
4  . . . # @ . . .
5  . . . @ # . . .
6  . . . . . . . .
7  . . . . . . . .
8  . . . . . . . .
# = 2 | @ = 2
Turn: @
$ PLAY 4C
```

# Contributing

Feel free to contribute by creating issues, submitting pull requests, or providing feedback.

1.Fork the repository.

2.Create a new branch (git checkout -b feature-branch).

3.Commit your changes (git commit -am 'Add new feature').

4.Push to the branch (git push origin feature-branch).

5.Create a new Pull Request.

# License

This project is open-source and available under the MIT License.

# Author
This is project was created by Kauã Borges, for testing and training Java/Kotlin.
"I actually made a version for java, and it will probably be released soon."
