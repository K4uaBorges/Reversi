/**
 * @author Kauã Borges
 * @file Color.kt
 *
 * This file defines the Color class (or enum) representing the
 * two possible piece colors in the game — Black and White.
 *
 * Each color is used to identify the player’s pieces and manage
 * game turns, flips, and scoring.
 *
 * Although simple, this file is essential for maintaining
 * clarity and consistency in player identification and logic.
 */


package model.piece

enum class ColorEnum {
    WHITE,
    BLACK,
}

data class Color(var color: ColorEnum)

fun Color.getColor(): ColorEnum = color

operator fun Color.not() : Color {
    val color = this.getColor()
    val newColor : ColorEnum = when (color) {
        ColorEnum.BLACK -> ColorEnum.WHITE
        ColorEnum.WHITE -> ColorEnum.BLACK
    }
    return Color(newColor)
}