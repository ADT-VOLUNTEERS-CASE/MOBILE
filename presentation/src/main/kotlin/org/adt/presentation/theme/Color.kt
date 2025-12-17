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