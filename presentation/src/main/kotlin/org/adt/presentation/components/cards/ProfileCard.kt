package org.adt.presentation.components.cards

import android.graphics.Color
import android.graphics.RenderEffect.createBlurEffect
import android.graphics.Shader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    renderEffect = createBlurEffect(
                        15f, 15f, Shader.TileMode.CLAMP
                    ).asComposeRenderEffect()
                    clip = true
                }
                .then(modifier))
        Row(
            Modifier
                .height(64.dp)
                .padding(16.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("") //TODO: use actual user avatar
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_single),
                contentDescription = "Profile image",
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = firstName,
                style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = 22.sp),
                fontWeight = FontWeight.SemiBold,
                color = VolunteersCaseTheme.colors.text,
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = "Navigate right arrow",
                tint = VolunteersCaseTheme.colors.text
            )
        }
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    VolunteersCaseTheme {
        Surface(color = DarkGray) { ProfileCard() }

    }
}