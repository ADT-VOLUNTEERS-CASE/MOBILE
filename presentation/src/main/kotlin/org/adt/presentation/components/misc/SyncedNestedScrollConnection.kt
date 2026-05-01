package org.adt.presentation.components.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class SyncedScrollState(
    val maxHeaderHeight: Float,
    val minHeaderHeight: Float,
    initialHeight: Float
) {
    var headerHeightPx by mutableFloatStateOf(initialHeight)

    val scaleFactor: Float
        get() = (headerHeightPx - minHeaderHeight) / (maxHeaderHeight - minHeaderHeight)

    val connection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            return if (delta < 0 && headerHeightPx > minHeaderHeight) {
                val oldHeight = headerHeightPx
                headerHeightPx = (headerHeightPx + delta).coerceAtLeast(minHeaderHeight)
                Offset(0f, -(oldHeight - headerHeightPx))
            } else Offset.Zero
        }

        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            return if (delta > 0 && headerHeightPx < maxHeaderHeight) {
                val oldHeight = headerHeightPx
                headerHeightPx = (headerHeightPx + delta).coerceAtMost(maxHeaderHeight)
                Offset(0f, headerHeightPx - oldHeight)
            } else Offset.Zero
        }
    }
}

@Composable
fun rememberSyncedScrollState(
    maxHeaderHeight: Float = 200f,
    minHeaderHeight: Float = 60f
): SyncedScrollState {
    return remember {
        SyncedScrollState(maxHeaderHeight, minHeaderHeight, maxHeaderHeight)
    }
}