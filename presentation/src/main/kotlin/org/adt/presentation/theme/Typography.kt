package org.adt.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.adt.presentation.R


//Example values

private val UnboundedFontFamily = FontFamily.SansSerif

private val InterDisplayFontFamily = FontFamily(
    Font(R.font.inter_display_black, FontWeight.Black),
    Font(R.font.inter_display_bold, FontWeight.Bold),
    Font(R.font.inter_display_semibold, FontWeight.SemiBold),
    Font(R.font.inter_display_medium, FontWeight.Medium),
    Font(R.font.inter_display_regular, FontWeight.Normal),
    Font(R.font.inter_display_light, FontWeight.Light),
    Font(R.font.inter_display_thin, FontWeight.Thin)
)

private val GerhausFontFamily = FontFamily(
    Font(R.font.gerhaus_regular, FontWeight.Normal),
)

@Immutable
data class AppTypography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

@SuppressLint("ComposeCompositionLocalUsage")
val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        titleSmall = TextStyle.Default,
        displayLarge = TextStyle.Default,
        displayMedium = TextStyle.Default,
        displaySmall = TextStyle.Default,
        labelLarge = TextStyle.Default,
        labelMedium = TextStyle.Default,
        labelSmall = TextStyle.Default
    )
}

val extendedTypography = AppTypography(
    displayLarge = TextStyle(),
    displayMedium = TextStyle(
        fontFamily = UnboundedFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp,
        lineHeight = 24.sp, letterSpacing = 0.5.sp, color = Brown
    ),
    displaySmall = TextStyle(
        fontFamily = InterDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.7.sp,
        color = Color.White
    ),
    titleLarge = TextStyle(
        fontFamily = UnboundedFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
        fontFamily = UnboundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(),
    labelLarge = TextStyle(),
    labelMedium = TextStyle(),
    labelSmall = TextStyle(
        fontFamily = UnboundedFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.7.sp,
        color = Golden
    )
)

val mainTypography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = InterDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        color = Graphite
    ),
    titleMedium = TextStyle(
        fontFamily = InterDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        color = Graphite
    ),
    titleSmall = TextStyle.Default,
    displayLarge = TextStyle(
        fontFamily = GerhausFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        color = Arctic
    ),
    displayMedium = TextStyle.Default,
    displaySmall = TextStyle.Default,
    labelLarge = TextStyle.Default,
    labelMedium = TextStyle.Default,
    labelSmall = TextStyle.Default
)