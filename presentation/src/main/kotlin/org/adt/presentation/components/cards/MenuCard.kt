package org.adt.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Interactive menu row component used in the profile screen with customizable icon and text
 *
 * @param title primary text header of the menu item
 *
 * @param subtitle secondary descriptive text displayed under the title
 *
 * @param icon image vector to be displayed as the leading visual anchor
 *
 * @param iconColor color tint applied to the icon and its tinted background container
 *
 * @param onClick function to be invoked when the menu card is clicked
 */
@Composable
fun MenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor)
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = VolunteersCaseTheme.typography.titleMedium,
                    color = Abyss
                )
                Text(
                    text = subtitle,
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = Graphite.copy(alpha = 0.6f)
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Graphite.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Profile Menu - List Example")
@Composable
private fun MenuCardPreview() {
    VolunteersCaseTheme {
        Column(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuCard(
                title = "Мои настройки",
                subtitle = "Изменить пароль и уведомления",
                icon = Icons.Default.Settings,
                iconColor = Lagoon,
                onClick = {}
            )

            MenuCard(
                title = "Служба поддержки",
                subtitle = "Задать вопрос координаторам",
                icon = Icons.Default.Info,
                iconColor = Mint,
                onClick = {}
            )

            MenuCard(
                title = "Выйти из аккаунта",
                subtitle = "Завершить текущую сессию",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                iconColor = Color(0xFFD32F2F),
                onClick = {}
            )
        }
    }
}
