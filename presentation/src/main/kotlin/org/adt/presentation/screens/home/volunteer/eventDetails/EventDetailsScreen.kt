package org.adt.presentation.screens.home.volunteer.eventDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.adt.core.entities.event.EventLocation
import org.adt.presentation.R
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.theme.VolunteersCaseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: EventDetailsViewModel,
) {
    EventDetailsScreenContent(
        modifier = modifier,
        uiState = viewModel.uiState.collectAsState().value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: EventDetailsState,
    backgroundImageOverride: Painter? = null,
) {
    //TODO: Add loading indication
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                title = { /* Nothing */ },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = { /* TODO: Navigate action! */ }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = VolunteersCaseTheme.colors.text
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageModifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))


            if (backgroundImageOverride != null) {
                Image(
                    painter = backgroundImageOverride,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } else {
                AsyncImage(
                    model = uiState.cover?.link,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.name,
                        color = VolunteersCaseTheme.colors.text,
                        style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 36.sp),
                        textAlign = TextAlign.Center
                    )

                    // TODO: TagsRow
                    Spacer(modifier = Modifier.height(24.dp))

                    // TODO:
                    //  ==========================================
                    //  REWRITE BELOW TO USE ACCORDING TYPOGRAPHY!
                    //  ==========================================

                    Text(
                        text = "О мероприятии",
                        color = VolunteersCaseTheme.colors.text,
                        fontWeight = FontWeight.SemiBold,
                        style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Описание",
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.5f),
                        style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.description,
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.9f),
                        style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Продолжительность",
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.5f),
                        style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        //TODO: Add duration to eventData!
                        text = "1 час 55 минут",
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.9f),
                        style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Координатор",
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.5f),
                        style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        //TODO: Add coordinator initials to eventData!
                        text = "Лимасов Андрей",
                        color = VolunteersCaseTheme.colors.text.copy(alpha = 0.9f),
                        style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FlowRow(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "Адрес проведения",
                                color = VolunteersCaseTheme.colors.text.copy(alpha = 0.5f),
                                style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.location.address,
                                color = VolunteersCaseTheme.colors.text.copy(alpha = 0.9f),
                                style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Дата проведения",
                                color = VolunteersCaseTheme.colors.text.copy(alpha = 0.5f),
                                style = VolunteersCaseTheme.typography.titleLarge.copy(fontSize = 14.sp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.localizedDateTime,
                                color = VolunteersCaseTheme.colors.text.copy(alpha = 0.9f),
                                style = VolunteersCaseTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    CustomButton(
                        text = "Подать заявку",
                        variant = ButtonVariant.RoughRounded,
                        modifier = Modifier.height(52.dp),
                    ) { }
                }
        }
    }
}

@Preview
@Composable
private fun EventDetailsScreenPreview() {
    VolunteersCaseTheme {
        EventDetailsScreenContent(
            uiState = EventDetailsState(
                name = "Уборка в парке",
                description = "Loreum ipsum",
                location = EventLocation(address = "Центральная улица"),
                localizedDateTime = "12 мая 19:07"
            ),
            backgroundImageOverride = painterResource(R.drawable.baseimage)
        )
    }
}