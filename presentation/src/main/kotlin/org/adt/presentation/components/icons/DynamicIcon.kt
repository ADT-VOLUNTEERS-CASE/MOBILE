package org.adt.presentation.components.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DynamicIcon(
    source: IconSource,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    contentDescription: String? = null
) {
    when (source) {
        is IconSource.Vector -> {
            Icon(
                imageVector = source.imageVector,
                modifier = modifier,
                contentDescription = contentDescription,
                tint = tint ?: Color.White
            )
        }

        is IconSource.Painter -> {
            Icon(
                painter = source.painter,
                modifier = modifier,
                contentDescription = contentDescription,
                tint = tint ?: Color.White
            )
        }
    }
}