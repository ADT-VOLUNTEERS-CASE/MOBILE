package org.adt.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Arctic = Color(0xFFFFFFFF)
val Graphite = Color(0xFF353535)
val Abyss = Color(0xFF24504F)
val Mint = Color(0xFFA7D9D9)
val Lagoon = Color(0xFF479391)
val Silver = Color(0xFFA1A4B2)
val Aqua = Color(0xFFCBEBEB)

@Immutable
data class AppColors(
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val tertiaryBackground: Color,
    val text: Color,
    val background: Color,
    val surface: Color
)

@SuppressLint("ComposeCompositionLocalUsage")
val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        primaryBackground = Color.Unspecified,
        secondaryBackground = Color.Unspecified,
        tertiaryBackground = Color.Unspecified,
        text = Color.Unspecified,
        background = Color.Unspecified,
        surface = Color.Unspecified
    )
}

val extendedColor = AppColors(
    primaryBackground = Abyss,
    secondaryBackground = Lagoon,
    tertiaryBackground = Mint,
    text = Arctic,
    background = Graphite,
    surface = Aqua
)