package org.adt.presentation.components.buttons

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ButtonConfig(
    val shape: Shape,
    val height: Dp? = null,
    val textStyle: TextStyle,
    val uppercase: Boolean = false,
    val loaderSize: Dp = 24.dp
)