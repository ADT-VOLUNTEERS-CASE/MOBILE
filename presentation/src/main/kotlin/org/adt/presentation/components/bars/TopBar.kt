package org.adt.presentation.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun TopBar(onDrawerToggleAction: () -> Unit= {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(VolunteersCaseTheme.colors.background)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                shape = CircleShape,
                onClick = {
                    onDrawerToggleAction.invoke()
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.MenuOpen,
                    contentDescription = "Open side menu",
                    modifier = Modifier.size(24.dp).rotate(180f),
                    tint = Color.White
                )
            }
        }
        Box(modifier = Modifier.weight(6f)){

        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    VolunteersCaseTheme {
        TopBar()
    }
}