package reversi_to_compose.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import reversi_compose.composeapp.generated.resources.*
import kotlin.math.roundToInt


val blackToWhite = listOf(
        Res.drawable.white,         Res.drawable.whitetoblack1, Res.drawable.whitetoblack2,
        Res.drawable.whitetoblack3, Res.drawable.whitetoblack4, Res.drawable.whitetoblack5,
        Res.drawable.whitetoblack6, Res.drawable.whitetoblack7, Res.drawable.black
)

val whiteToBlack = listOf(
    Res.drawable.black,         Res.drawable.whitetoblack7, Res.drawable.whitetoblack6,
    Res.drawable.whitetoblack5, Res.drawable.whitetoblack4, Res.drawable.whitetoblack3,
    Res.drawable.whitetoblack2, Res.drawable.whitetoblack1, Res.drawable.white
)
@Composable
fun AnimatedPieceSpriteFlip(

    triggerKey: Any,
    frames: List<DrawableResource>,
    modifier: Modifier = Modifier,
    durationMillis: Int = 500,  // 500 equal 0,5 seconds
    onAnimationEnd: () -> Unit
): Painter {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(triggerKey) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(durationMillis))
        onAnimationEnd()
    }

    val n = frames.size
    val idx = ((progress.value * (n - 1)).roundToInt()).coerceIn(0, n - 1)

    Image(
        painter = painterResource(frames[idx]),
        contentDescription = "piece flip",
        modifier = modifier
    )
    return painterResource(frames[idx])
}

