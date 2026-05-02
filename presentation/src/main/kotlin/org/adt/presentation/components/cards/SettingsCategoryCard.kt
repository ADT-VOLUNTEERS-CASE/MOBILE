package org.adt.presentation.components.cards

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.components.shaders.ShaderBox
import org.adt.presentation.theme.VolunteersCaseTheme
import org.adt.presentation.utils.ShaderPresets

@Composable
fun SettingsCategoryCard(
    modifier: Modifier = Modifier,
    title: String = "Example category",
    description: String = "Configure your settings here",
    contentDescription: String = "",
    iconVector: ImageVector = Icons.Default.Settings,
    onClickAction: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .widthIn(min = 300.dp)
            .heightIn(min = 60.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClickAction)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.DarkGray.copy(alpha = 0.4f))
                .blur(radius = 12.dp)
                .border(
                    width = 0.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Gray.copy(alpha = 0.2f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(7f)
            ) {
                Text(
                    text = title,
                    style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 13.sp),
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 12.dp)
                    .weight(1.5f),
                contentAlignment = Alignment.TopCenter
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = iconVector,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        }
    }
}


@Preview
@Composable
private fun SettingsCategoryCardPreview() {
    VolunteersCaseTheme {
        ShaderBox(modifier = Modifier, preset = ShaderPresets.DarkGrayBackground) {
            SettingsCategoryCard()
        }
    }
}