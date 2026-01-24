package reversi_to_compose.compose

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.jetbrains.compose.resources.*


import reversi_to_compose.compose.NewOrJoinType.*
import reversi_to_compose.model.piece.Color
import reversi_to_compose.model.piece.Color.*
import reversi_to_compose.model.piece.*

enum class NewOrJoinType(val txt: String) {
    NewGameEnum("New Game"),
    JoinEnum("Join")
}


@Composable
fun IconButton(
    color: Color,
    contentDescription: String = "Color",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var triggerKey by remember { mutableIntStateOf(0) }
    var isAnimated by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        enabled = !isAnimated,
        onClick = {
            triggerKey++
            isAnimated = true
            onClick()
        },
        interactionSource = interactionSource
    ) {
        if (isAnimated) {
            AnimatedPieceSpriteFlip(
                triggerKey = triggerKey,
                frames = if (color == WHITE) whiteToBlack else blackToWhite,
                modifier = if (isPressed) Modifier.alpha(0.9f) else Modifier,
                onAnimationEnd = { isAnimated = false }
            )
        } else {
            Image(
                painter = painterResource(resourse(color)),
                contentDescription = contentDescription,
                modifier = if (isPressed) Modifier.alpha(0.9f) else Modifier,
            )
        }
    }
}


operator fun NewOrJoinType.not(): NewOrJoinType {

    val newColor: NewOrJoinType = when (this) {
        NewGameEnum -> JoinEnum
        JoinEnum -> NewGameEnum
    }

    return newColor
}

@Composable
fun newGameJoinDialog(
    type: NewOrJoinType,
    close: () -> Unit,
    newGameJoinAction: (String?, Color) -> Unit
) {
    var name: String? by remember { mutableStateOf(if (type == JoinEnum) "" else null) }
    var color: Color by remember { mutableStateOf(BLACK) }
    val fr = remember { FocusRequester() }
    var selectedType by remember { mutableStateOf(type) }
    val isJoin = selectedType == JoinEnum
    val isNameValid = name?.isNotBlank().also { name?.all { it.isLetterOrDigit() } }

    fun keyHandler(ke: KeyEvent): Boolean {
        if (ke.key == Key.Enter && ke.type == KeyEventType.KeyDown) {
            val nameValue = if (isJoin) name!! else null
            if (!isJoin || nameValue != null) {
                newGameJoinAction(nameValue, color)
            }
            return true
        }
        return false
    }

    DialogWindow(
        onCloseRequest = close,
        title = NewGameEnum.txt,
        icon = painterResource(resourse(color)),
        state = rememberDialogState(
            width = 350.dp, height = if (isJoin) 300.dp else 250.dp
        ),
        resizable = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)
                    .widthIn(min = 250.dp, max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isJoin, onCheckedChange = { checked ->
                            selectedType = if (checked) JoinEnum else NewGameEnum
                            if (checked) {
                                if (name == null) name = ""
                            } else {
                                name = null
                            }
                        })
                    Spacer(Modifier.width(8.dp))
                    Text("Multiplayer")
                }
                if (!isJoin) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { color = color.not() },
                            color = color,
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Game name:", modifier = Modifier.width(100.dp)
                        )
                        OutlinedTextField(
                            value = name ?: "",
                            onValueChange = { newValue -> name = newValue },
                            singleLine = true,
                            modifier = Modifier.weight(1f).onKeyEvent(::keyHandler).focusRequester(fr)
                        )
                        IconButton(
                            onClick = { color = color.not() },
                            color = color,
                        )
                    }
                }
                Button(
                    enabled = if (isJoin) {
                        isNameValid == true
                    } else {
                        true
                    },
                    onClick = {
                        val nameValue = if (isJoin) name?.takeIf { it.isNotBlank() } else null
                        newGameJoinAction(nameValue, color)
                    },
                ) {
                    Text("New game")
                }
            }
        }
    }
}


@Composable
fun joinDialog(
    close: () -> Unit,
    joinAction: (String) -> Unit
) {
    var name: String by remember { mutableStateOf("") }
    val fr = remember { FocusRequester() }
    val isNameValid = name.isNotBlank().also { name.all { it.isLetterOrDigit() } }

    fun keyHandler(ke: KeyEvent): Boolean {
        if (ke.key == Key.Enter && ke.type == KeyEventType.KeyDown) {
            if (isNameValid) {
                joinAction(name)
            }
            return true
        }
        return false
    }

    DialogWindow(
        onCloseRequest = close,
        title = JoinEnum.txt,
        icon = painterResource(resourse(BLACK)),
        state = rememberDialogState(
            width = 300.dp, height = 250.dp
        ),
        resizable = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)
                    .widthIn(min = 250.dp, max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Game name:", modifier = Modifier.width(100.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { newValue -> name = newValue },
                        singleLine = true,
                        modifier = Modifier.weight(1f).onKeyEvent(::keyHandler).focusRequester(fr)
                    )
                }
            }
            Button(
                enabled = isNameValid,
                onClick = { joinAction(name) },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text("Join Game")
            }
        }
    }

    LaunchedEffect(true) {
        fr.requestFocus()
    }
}

