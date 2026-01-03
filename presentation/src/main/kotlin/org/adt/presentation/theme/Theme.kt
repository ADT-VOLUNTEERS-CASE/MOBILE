package org.adt.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun VolunteersCaseTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides extendedColor,
        LocalAppTypography provides extendedTypography
    ) {
        @Suppress("UNCHECKED_CAST") // Screenshot support, composable casting inside test package is not available
        MaterialTheme(
            content = content
        )
    }
}

object VolunteersCaseTheme {
    val colors: AppColors
        @Composable get() = LocalAppColors.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
}