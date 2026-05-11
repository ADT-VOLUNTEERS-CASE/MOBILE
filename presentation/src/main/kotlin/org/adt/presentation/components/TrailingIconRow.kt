package org.adt.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.components.icons.DynamicIcon
import org.adt.presentation.components.icons.IconSource
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun TrailingIconRow(modifier: Modifier = Modifier, icon: IconSource, text: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DynamicIcon(
            source = icon,
            modifier = Modifier.size(14.dp),
            tint = Color.White.copy(alpha = 0.7f)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Preview
@Composable
private fun TrailingIconRowPreview() {
    VolunteersCaseTheme {
        TrailingIconRow(
            text = "",
            modifier = Modifier.fillMaxWidth(),
            icon = IconSource.Vector(Icons.Default.LocationOn)
        )
    }
}