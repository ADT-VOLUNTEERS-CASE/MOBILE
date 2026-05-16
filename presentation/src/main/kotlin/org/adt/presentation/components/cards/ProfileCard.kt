package org.adt.presentation.components.cards

import android.graphics.RenderEffect.createBlurEffect
import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.adt.presentation.R
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
//TODO: rename UserResponse class
fun ProfileCard(
    modifier: Modifier = Modifier,
    firstName: String = "Пользователь",
    scaleFactor: Float = 0f,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)

    ) {
        Row(
            modifier
                .padding(top = (2 * scaleFactor).dp)
                .height((64 + (16 * scaleFactor)).dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(7f),
                text = firstName,
                style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = (22 + (6 * scaleFactor * 0.8f)).sp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .weight(2f)
                    .size(16.dp),
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = "Navigate right arrow",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    VolunteersCaseTheme {
        Surface(color = DarkGray) { ProfileCard(scaleFactor = 1f) }

    }
}