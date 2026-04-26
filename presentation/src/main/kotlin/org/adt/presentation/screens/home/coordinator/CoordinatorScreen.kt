package org.adt.presentation.screens.home.coordinator

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.adt.core.entities.Location
import org.adt.core.entities.UserRole
import org.adt.presentation.components.CustomBottomBar
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.TypingText
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.components.cards.ApplicationCard
import org.adt.presentation.components.cards.EventSummaryCard
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Grey
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.Void
import org.adt.presentation.theme.VolunteersCaseTheme
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CoordinatorScreen(
    navController: NavHostController,
    viewModel: CoordinatorViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val fields = viewModel.fieldsState.collectAsState().value

    CoordinatorScreenContent(
        uiState = uiState,
        fields = fields,
        onUploadFile = { file -> viewModel.uploadCover(file) },
        onSearchLocation = { viewModel.findLocation(it) },
        onSelectLocation = { viewModel.selectLocation(it) },
        updateFields = { viewModel.updateInputs(it) },
        createAction = { viewModel.onCreateEventClick() },
        clearMessage = { viewModel.clearMessage() },
        logoutAction = {
            viewModel.deauthenticate {
                navController.navigate(Destinations.Splash) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        },
        setShowDatePicker = { viewModel.setShowDatePicker(it) },
        setShowTimePicker = { viewModel.setShowTimePicker(it) },
        onApprove = { eventId, userId -> viewModel.approve(eventId, userId) },
        onReject = { eventId, userId -> viewModel.reject(eventId, userId) },
        onLoadApplications = { eventId -> viewModel.loadApplications(eventId) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatorScreenContent(
    uiState: CoordinatorState = CoordinatorState(),
    fields: CoordinatorFieldsState = CoordinatorFieldsState(),
    onUploadFile: (File) -> Unit = {},
    onSearchLocation: (String) -> Unit = {},
    onSelectLocation: (Location) -> Unit = {},
    updateFields: (CoordinatorFieldsState) -> Unit = {},
    createAction: () -> Unit = {},
    clearMessage: () -> Unit = {},
    logoutAction: () -> Unit = {},
    setShowDatePicker: (Boolean) -> Unit = {},
    setShowTimePicker: (Boolean) -> Unit = {},
    onApprove: (Long, Long) -> Unit = { _, _ -> },
    onReject: (Long, Long) -> Unit = { _, _ -> },
    onLoadApplications: (Long) -> Unit = {},
    animationOverride: Boolean = false
) {
    val context = LocalContext.current
    val visualFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val file = CoordinatorFileUtils.getFileFromUri(context, it)
            if (file != null) onUploadFile(file)
        }
    }

    LaunchedEffect(uiState.createError) {
        uiState.createError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            clearMessage()
        }
    }

    if (uiState.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= System.currentTimeMillis() - 86400000L
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { setShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    if (selectedDate != null) {
                        val currentDt = fields.selectedDateTime ?: LocalDateTime.now()
                        updateFields(fields.copy(selectedDateTime = selectedDate.atTime(currentDt.toLocalTime())))
                        setShowDatePicker(false)
                        setShowTimePicker(true)
                    }
                }) { Text("ОК", color = Mint) }
            },
            dismissButton = {
                TextButton(onClick = { setShowDatePicker(false) }) {
                    Text(
                        "Отмена",
                        color = Arctic
                    )
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Abyss.copy(0.6f))
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Arctic,
                    titleContentColor = Graphite,
                    headlineContentColor = Graphite,
                    dividerColor = Graphite.copy(0.4f),
                    weekdayContentColor = Graphite,
                    dayContentColor = Graphite,
                    selectedDayContainerColor = Lagoon,
                    selectedDayContentColor = Void,
                    todayContentColor = Lagoon,
                    todayDateBorderColor = Color.Transparent,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Arctic,
                        unfocusedContainerColor = Arctic,
                        focusedTextColor = Void,
                        unfocusedTextColor = Grey,
                        cursorColor = Graphite,
                        unfocusedIndicatorColor = Graphite,
                        focusedIndicatorColor = Graphite,
                        selectionColors = TextSelectionColors(Graphite, Graphite.copy(0.2f))
                    )
                )
            )
        }
    }

    if (uiState.showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { setShowTimePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    val date =
                        fields.selectedDateTime?.toLocalDate() ?: LocalDateTime.now().toLocalDate()
                    val finalDateTime = date.atTime(timePickerState.hour, timePickerState.minute)

                    if (finalDateTime.isBefore(LocalDateTime.now())) {
                        Toast.makeText(context, "Время уже прошло!", Toast.LENGTH_SHORT).show()
                    } else {
                        updateFields(
                            fields.copy(
                                selectedDateTime = finalDateTime,
                                dateTimestamp = finalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                            )
                        )
                        setShowTimePicker(false)
                    }
                }) { Text("ОК", color = Lagoon) }
            },
            dismissButton = {
                TextButton(onClick = { setShowTimePicker(false) }) {
                    Text(
                        "Отмена",
                        color = Arctic
                    )
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Graphite.copy(0.9f),
                        clockDialSelectedContentColor = Abyss,
                        clockDialUnselectedContentColor = Arctic,
                        selectorColor = Mint,
                        periodSelectorSelectedContainerColor = Mint,
                        periodSelectorBorderColor = Graphite.copy(0.8f),
                        periodSelectorSelectedContentColor = Lagoon,
                        periodSelectorUnselectedContentColor = Void,
                        periodSelectorUnselectedContainerColor = Arctic,
                        timeSelectorSelectedContainerColor = Mint,
                        timeSelectorSelectedContentColor = Abyss,
                        timeSelectorUnselectedContentColor = Void,
                        timeSelectorUnselectedContainerColor = Graphite.copy(0.1f)
                    )
                )
            },
            containerColor = Arctic
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Abyss)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState()),
            Arrangement.spacedBy(20.dp),
            Alignment.CenterHorizontally
        ) {
            TypingText(
                Modifier.padding(top = 100.dp),
                text = "Твоё следующее доброе дело ждёт своего момента",
                charDelay = if (animationOverride) 0L else 40L,
                animationOverride = animationOverride
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VolunteersCaseTheme.colors.secondaryBackground)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                Text(
                    "Создание\nмероприятия",
                    style = VolunteersCaseTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth()
                )

                CustomTextField(Modifier, "Название") { updateFields(fields.copy(name = it)) }

                CustomTextField(
                    Modifier,
                    "Описание"
                ) { updateFields(fields.copy(description = it)) }
                CustomTextField(
                    Modifier,
                    "Макс. участников"
                ) { updateFields(fields.copy(maxCapacity = it.toLongOrNull() ?: 0)) }

                CustomTextField(Modifier, "ID Тегов (через запятую)") {
                    val ids = it.split(",").mapNotNull { id -> id.trim().toLongOrNull() }
                    updateFields(fields.copy(tagIds = ids))
                }

                CustomButton(
                    text = if (fields.selectedDateTime != null)
                        "Дата: ${fields.selectedDateTime.format(visualFormatter)}"
                    else "Выбрать дату и время",
                    onClick = { setShowDatePicker(true) },
                    style = ButtonStyle.Filled
                )

                CustomButton(
                    text = if (uiState.selectedCover != null) "Обложка загружена" else "Выбрать обложку",
                    isLoading = uiState.isUploadingCover,
                    onClick = { launcher.launch("image/*") },
                    style = ButtonStyle.Filled
                )

                Text("Локация", style = VolunteersCaseTheme.typography.titleLarge)

                CustomSearchTextField(
                    label = "Введите адрес для поиска",
                    value = "",
                    onConfirm = { onSearchLocation(it) },
                    onValueChange = {}
                )

                if (uiState.isSearchMode) {
                    if (uiState.searchLoading) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(top = 32.dp),
                                color = Mint
                            )
                        }
                    } else {
                        Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                            uiState.searchResults.forEach { location ->
                                Text(
                                    location.address,
                                    style = VolunteersCaseTheme.typography.titleMedium.copy(Graphite),
                                    modifier = Modifier.clickable { onSelectLocation(location) }
                                )
                            }
                        }
                    }
                }

                uiState.selectedLocation?.let {
                    Text(
                        "Выбрано: ${it.address}",
                        color = Abyss,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                CustomButton(
                    text = "Опубликовать",
                    enabled = fields.name.isNotBlank() &&
                            uiState.selectedLocation != null &&
                            uiState.selectedCover != null &&
                            fields.selectedDateTime != null,
                    isLoading = uiState.isLoading,
                    onClick = createAction,
                    variant = ButtonVariant.Wide
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VolunteersCaseTheme.colors.secondaryBackground)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "Мои мероприятия",
                    style = VolunteersCaseTheme.typography.titleLarge,
                    color = Graphite
                )

                if (uiState.myEvents.isNotEmpty()) {
                    uiState.myEvents.forEach { event ->
                        EventSummaryCard(event = event) {
                            onLoadApplications(event.eventId)
                        }
                    }
                } else {
                    Text(
                        "Пока пусто...",
                        style = VolunteersCaseTheme.typography.titleMedium.copy(Graphite)
                    )
                }

                if (uiState.applications.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Заявки на участие",
                        style = VolunteersCaseTheme.typography.titleLarge,
                        color = Graphite
                    )
                    uiState.applications.forEach { app ->
                        ApplicationCard(
                            app = app,
                            onApprove = { onApprove(app.eventId.toLong(), app.userId.toLong()) },
                            onReject = { onReject(app.eventId.toLong(), app.userId.toLong()) }
                        )
                    }
                }
            }

            TextButton(logoutAction, contentPadding = PaddingValues(2.dp)) {
                Text(
                    "Выйти",
                    style = VolunteersCaseTheme.typography.titleMedium.copy(
                        Arctic,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Spacer(Modifier.height(100.dp))
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomBottomBar(
                Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 15.dp),
                UserRole.COORDINATOR, Destinations.CoordinatorHome
            ) { }
        }
    }
}

object CoordinatorFileUtils {
    fun getFileFromUri(context: android.content.Context, uri: android.net.Uri): File? {
        return try {
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Preview
@Composable
private fun CoordinatorScreenPreview() {
    VolunteersCaseTheme {
        CoordinatorScreenContent(animationOverride = true)
    }
}