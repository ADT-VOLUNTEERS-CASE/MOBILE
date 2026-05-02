package org.adt.presentation.utils

import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@JvmInline
value class Shader(val path: String)

enum class ShaderPresets(val shader: Shader) {
    DarkGreenBackground(Shader("shaders/LiquidNoise.glsl")),
    DarkGrayBackground(Shader("shaders/GrayNoise.glsl"))
}

@Composable
fun loadShaderFromAssets(shaderPreset: ShaderPresets): RuntimeShader {
    val context = LocalContext.current
    val path = shaderPreset.shader.path

    return remember(path) {
        RuntimeShader(context.assets.open(path).bufferedReader().use { it.readText() })
    }
}
