package org.adt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.R
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
        IconButton(
            modifier = Modifier
                .weight(4f)
                .size(24.dp),
            shape = CircleShape,
            onClick = {
                onDrawerToggleAction.invoke()
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Open side menu",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
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