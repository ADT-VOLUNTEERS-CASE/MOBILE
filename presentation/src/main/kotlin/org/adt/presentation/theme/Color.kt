package org.adt.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Arctic = Color(0xFFFFFFFF)
val Graphite = Color(0xFF353535)
//val BaseColor = Color(0xFF009ADA)

val Abyss = Color(0xFF003047)
val Mint = Color(0xFFBCE3F2)
val Lagoon = Color(0xFF3CA8CE)
val Silver = Color(0xFFA1A4B2)
val Aqua = Color(0xFFCBEBEB)
val Milk = Color(0xFFF3F3F3)
val Void = Color(0xFF181718)
val Black = Color(0xFF000000)
val Grey = Color(0xFFA1A4B2)

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
    secondaryBackground = Arctic,
    tertiaryBackground = Mint,
    text = Arctic,
    background = Graphite,
    surface = Aqua
)

