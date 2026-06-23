package org.adt.presentation.components.cards


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.adt.core.entities.event.EventApplication
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonColorScheme
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme

/**
 * Card displaying volunteer event application details with action buttons
 *
 * @param app object containing volunteer application data like name and email
 *
 * @param onApprove function to be invoked when the "Approve" button is clicked
 *
 * @param onReject function to be invoked when the "Reject" button is clicked
 *
 * @sample [ApplicationCardPreview]
 */
@Composable
fun ApplicationCard(
    app: EventApplication,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column (
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${app.firstname} ${app.lastname}",
                style = VolunteersCaseTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Void
            )


            val dateText = app.createdAt?.substringBefore("T") ?: ""
            if (dateText.isNotBlank()) {
                Text(
                    text = dateText,
                    style = VolunteersCaseTheme.typography.labelSmall,
                    color = Graphite.copy(alpha = 0.4f)
                )
            }
        }


        Text(
            text = app.email,
            color = Lagoon,
            style = VolunteersCaseTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            CustomButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.button_cancel),
                style = ButtonStyle.Outlined,
                variant = ButtonVariant.LiteRounded,
                colors = ButtonColorScheme(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFD32F2F).copy(alpha = 0.7f),
                    borderColor = Color(0xFFD32F2F).copy(alpha = 0.2f)
                ),
                onClick = onReject
            )

            CustomButton(
                modifier = Modifier.weight(1.5f),
                text = stringResource(R.string.button_accept),
                style = ButtonStyle.Filled,
                variant = ButtonVariant.LiteRounded,
                colors = ButtonColorScheme(
                    containerColor = Lagoon,
                    contentColor = Color.White
                ),
                onClick = onApprove
            )
        }
    }
}

@Preview(showBackground = true, name = "Application Card - Active")
@Composable
private fun ApplicationCardPreview() {
    val mockApp = remember {
        EventApplication(
            eventId = 42,
            eventName = "Экологический форум",
            userId = 107,
            firstname = "Александр",
            lastname = "Верховный",
            patronymic = "Сергеевич",
            phoneNumber = "+79991234567",
            email = "alex.volunteer@adt.org",
            status = "PENDING",
            rejectReason = null,
            createdAt = "2026-05-28T12:00:00",
            rejectedAt = null,
            revokedAt = null
        )
    }

    VolunteersCaseTheme {
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp,
                color = Arctic
            ) {
                ApplicationCard(
                    app = mockApp,
                    onApprove = {},
                    onReject = {}
                )
            }
        }
    }
}

