package org.adt.presentation.screens.home.volunteer.eventDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonDefaultsProvider
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.theme.*

@Composable
fun EventDetailsScreen(
    onBackNavigationAction: () -> Unit,
    viewModel: EventDetailsViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    EventDetailsScreenContent(
        uiState = uiState,
        onApplicationSendAction = viewModel::sendEventApplication,
        onBackNavigationAction = onBackNavigationAction
    )
}

@Composable
fun EventDetailsScreenContent(
    uiState: EventDetailsState,
    onApplicationSendAction: () -> Unit = {},
    onBackNavigationAction: () -> Unit = {},
    backgroundImageOverride: Painter? = null,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Arctic,
        bottomBar = {
            BottomActionSurface(uiState, onApplicationSendAction)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                if (backgroundImageOverride != null) {
                    Image(
                        painter = backgroundImageOverride,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = uiState.cover?.link,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Arctic.copy(alpha = 0.8f),
                                    Arctic
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(340.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Arctic
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Abyss,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 38.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        InfoRowGrid(uiState)

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Описание",
                            style = VolunteersCaseTheme.typography.titleLarge,
                            color = Abyss,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Graphite.copy(alpha = 0.8f),
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Координатор",
                            style = VolunteersCaseTheme.typography.titleLarge,
                            color = Abyss,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CoordinatorProfileCard("Лимасов Андрей")

                        Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 100.dp))
                    }
                }
            }

            IconButton(
                onClick = onBackNavigationAction,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(44.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Abyss,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoRowGrid(uiState: EventDetailsState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DetailItem(
                icon = Icons.Default.CalendarToday,
                label = "Дата",
                value = uiState.localizedDateTime,
                modifier = Modifier.weight(1f)
            )
            DetailItem(
                icon = Icons.Default.AccessTime,
                label = "Длительность",
                value = "1 ч. 55 мин.",
                modifier = Modifier.weight(1f)
            )
        }
        DetailItem(
            icon = Icons.Default.LocationOn,
            label = "Адрес",
            value = uiState.location.address,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Mint, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(label, style = VolunteersCaseTheme.typography.labelSmall, color = Graphite.copy(0.5f))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Abyss,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CoordinatorProfileCard(name: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Mint.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Mint)
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = name,
                style = VolunteersCaseTheme.typography.titleMedium,
                color = Abyss,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BottomActionSurface(uiState: EventDetailsState, onApply: () -> Unit) {
    val status = uiState.applicationStatus
    val isRegistered = status.isNotEmpty() && status != "REJECTED"

    val (btnText, btnColor) = when (status) {
        "PENDING", "ALREADY_EXISTS" -> "Заявка отправлена" to Color(0xFFFBC02D)
        "SUCCESS" -> "Вы участвуете" to Mint
        else -> "Принять участие" to Lagoon
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp,
        tonalElevation = 8.dp
    ) {
        Box(Modifier.navigationBarsPadding().padding(20.dp)) {
            CustomButton(
                text = btnText,
                variant = ButtonVariant.RoughRounded,
                colors = ButtonDefaultsProvider.colors(
                    ButtonVariant.RoughRounded,
                    ButtonStyle.Filled,
                    true
                ).copy(
                    containerColor = btnColor,
                    contentColor = if (status == "SUCCESS") Color.White else Abyss
                ),
                enabled = !isRegistered,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                onClick = onApply
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailsScreenPreview() {
    VolunteersCaseTheme {
        EventDetailsScreenContent(
            uiState = EventDetailsState(
                name = "Городской субботник: Чистая весна",
                description = "Самое масштабное экологическое событие сезона. Мы объединяем усилия, чтобы привести в порядок наш любимый городской парк. Инструменты, перчатки и легкий перекус предоставляются организаторами.",
                location = EventLocation(address = "ул. Ленина, Центральный парк"),
                localizedDateTime = "24 мая, 10:00"
            ),
            backgroundImageOverride = painterResource(R.drawable.baseimage)
        )
    }
}