package org.adt.presentation.components.icons

import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconSource {
    data class Vector(val imageVector: ImageVector) : IconSource()
    data class Painter(val painter: androidx.compose.ui.graphics.painter.Painter): IconSource()
}