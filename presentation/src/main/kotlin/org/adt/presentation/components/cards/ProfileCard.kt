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
import androidx.compose.ui.graphics.Color.Companion.DarkGray
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
    scaleFactor: Float = 0f,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)

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
        )
        Row(
            modifier
                .padding(top = (36 * scaleFactor).dp)
                .height((64 + (16 * scaleFactor)).dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fpreview.redd.it%2Fbattle-cats-icons-v0-9hjbm5yawvoc1.png%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D16d5c788048900222349f5b3fc944f715ba732c1&f=1&nofb=1&ipt=4e53ca0e899cc097b600dc2bd46b31f04147785dc97dcb176fa6d05d919c35ce") //TODO: use actual user avatar
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_single),
                contentDescription = "Profile image",
            )

            Text(
                modifier = Modifier
                    .weight(6f),
                text = firstName,
                style = VolunteersCaseTheme.typography.labelLarge.copy(fontSize = (22 + (8 * scaleFactor * 0.8f)).sp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = VolunteersCaseTheme.colors.text,
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .weight(2f)
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
        Surface(color = DarkGray) { ProfileCard(scaleFactor = 1f) }

    }
}