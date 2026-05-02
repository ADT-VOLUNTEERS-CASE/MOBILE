package org.adt.presentation.components.shaders

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.utils.ShaderPresets
import org.adt.presentation.utils.loadShaderFromAssets

@Composable
fun ShaderBox(
    modifier: Modifier = Modifier,
    preset: ShaderPresets,
    shape: Shape = RectangleShape,
    content: @Composable (BoxScope) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val shader = loadShaderFromAssets(preset)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                shader.setFloatUniform("iTime", time)
                shader.setFloatUniform("iResolution", size.width, size.height)
                val brush = ShaderBrush(shader)

                val outline = shape.createOutline(size, layoutDirection, this)

                onDrawBehind {
                    drawOutline(outline, brush)
                }
            }
            .then(modifier),
        content = content
    )
}

@Preview
@Composable
private fun ShaderSurfacePreview() {
    ShaderBox(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(32.dp),
        preset = ShaderPresets.DarkGreenBackground
    ) { }
}