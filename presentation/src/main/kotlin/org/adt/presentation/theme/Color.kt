package org.adt.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//Example values

val Golden = Color(0xFFCE9760)
val LightBrown = Color(0xFFCE9760)
val Brown = Color(0xFF543A20)

@Immutable
data class AppColors(
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val tertiaryBackground: Color,
    val text: Color
)

@SuppressLint("ComposeCompositionLocalUsage")
val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        primaryBackground = Color.Unspecified,
        secondaryBackground = Color.Unspecified,
        tertiaryBackground = Color.Unspecified,
        text = Color.Unspecified
    )
}

val extendedColor = AppColors(
    primaryBackground = Brown,
    secondaryBackground = Golden,
    tertiaryBackground = LightBrown,
    text = Color.White
)

val Arctic = Color(0xFFFFFFFF)
val Graphite = Color(0xFF353535)
val Abyss = Color(0xFF24504F)
val Mint = Color(0xFFA7D9D9)
val Lagoon = Color(0xFF479391)
val Silver = Color(0xFFA1A4B2)
val Aqua = Color(0xFFCBEBEB)
val Black = Color(0xFF000000)
val Grey = Color(0xFFA1A4B2)