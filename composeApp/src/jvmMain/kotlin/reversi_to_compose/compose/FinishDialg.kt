package reversi_to_compose.compose

import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.*

import reversi_to_compose.model.piece.Color.*

@Composable
    fun finishDialg(
        close: () -> Unit,
        onExit: () -> Unit,
        restart: () -> Unit,
    ) {
        DialogWindow(
            onCloseRequest = onExit,
            title = "Finish",
            icon = painterResource(resourse(BLACK)),
            state = rememberDialogState(width = 350.dp, height = 200.dp),
            resizable = false,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 22.dp, vertical = 18.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Game already finished. Do you wish to restart?",
                        maxLines = 1
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                restart()
                                close()
                            }
                        ) {
                            Text("Yes")
                        }

                        Button(
                            onClick = { onExit() }
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }


