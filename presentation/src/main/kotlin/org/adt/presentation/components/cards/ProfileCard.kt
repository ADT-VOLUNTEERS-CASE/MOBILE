package org.adt.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Dynamic reactive profile card component with interpolation layout behavior
 *
 * Designed to reside within collapsing containers (like top navigation bars).
 * Dynamically adjusts its inner paddings, height boundaries, and font sizes
 * based on a linear interpolation [scaleFactor] token to enable smooth structural transformations.
 *
 * @param modifier modifier used for managing external sizing layout anchors, boundaries, and alignment
 * @param firstName primary identity text header representing the user's name
 * @param scaleFactor progressive interpolation value (from 0.0 to 1.0) defining the scale state and text dimension bounds
 * @param onClick function to be invoked when the bounded area of the profile card layout is pressed
 *
 * @sample [ProfileCardPreview]
 */
@Composable
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
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            ProfileCard(scaleFactor = 1f)
        }
    }
}