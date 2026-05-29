package org.adt.presentation.screens.home.coordinator.home

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.adt.core.entities.Location
import org.adt.core.entities.event.EventApplication
import org.adt.presentation.components.CustomSearchTextField
import org.adt.presentation.components.CustomTextField
import org.adt.presentation.components.bars.SyncedTopNavigationBarCoordinator
import org.adt.presentation.components.buttons.ButtonVariant
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.components.cards.ApplicationCard
import org.adt.presentation.components.cards.EventSummaryCard
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Lagoon
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.Mint
import org.adt.presentation.theme.Silver
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
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val fields = viewModel.fieldsState.collectAsState().value
    val scope = rememberCoroutineScope()

    CoordinatorScreenContent(
        uiState = uiState,
        fields = fields,
        isRefreshing = isRefreshing,
        onUploadFile = { file -> viewModel.uploadCover(file) },
        onSearchLocation = { viewModel.findLocation(it) },
        onSelectLocation = { viewModel.selectLocation(it) },
        updateFields = { viewModel.updateInputs(it) },
        createAction = { viewModel.onCreateEventClick() },
        clearMessage = { viewModel.clearMessage() },
        setShowDatePicker = { viewModel.setShowDatePicker(it) },
        setShowTimePicker = { viewModel.setShowTimePicker(it) },
        onApprove = { eventId, userId -> viewModel.approve(eventId, userId) },
        onReject = { eventId, userId -> viewModel.reject(eventId, userId) },
        onLoadApplications = { eventId -> viewModel.loadApplications(eventId) },
        onRefreshAction = {
            scope.launch {
                viewModel.loadMyEvents()
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatorScreenContent(
    uiState: CoordinatorState = CoordinatorState(),
    isRefreshing: Boolean = false,
    fields: CoordinatorFieldsState = CoordinatorFieldsState(),
    onUploadFile: (File) -> Unit = {},
    onSearchLocation: (String) -> Unit = {},
    onSelectLocation: (Location) -> Unit = {},
    updateFields: (CoordinatorFieldsState) -> Unit = {},
    createAction: () -> Unit = {},
    clearMessage: () -> Unit = {},
    setShowDatePicker: (Boolean) -> Unit = {},
    setShowTimePicker: (Boolean) -> Unit = {},
    onApprove: (Long, Long) -> Unit = { _, _ -> },
    onReject: (Long, Long) -> Unit = { _, _ -> },
    onLoadApplications: (Long) -> Unit = {},
    onRefreshAction: () -> Unit = {},
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
                }) {
                    Text(
                        "Далее",
                        color = Lagoon,
                        style = VolunteersCaseTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowDatePicker(false) }) {
                    Text(
                        "Отмена",
                        color = Graphite.copy(0.4f),
                        style = VolunteersCaseTheme.typography.labelLarge
                    )
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = DatePickerDefaults.colors(containerColor = Milk)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Milk,
                    titleContentColor = Graphite.copy(alpha = 0.6f),
                    headlineContentColor = Abyss,
                    selectedDayContainerColor = Lagoon,
                    selectedDayContentColor = Color.White,
                    todayContentColor = Lagoon,
                    todayDateBorderColor = Lagoon.copy(alpha = 0.3f),
                    dayContentColor = Void,
                    weekdayContentColor = Graphite.copy(alpha = 0.5f),
                    navigationContentColor = Graphite
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
                }) {
                    Text(
                        "Готово",
                        color = Lagoon,
                        style = VolunteersCaseTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { setShowTimePicker(false) }) {
                    Text(
                        "Назад",
                        color = Graphite.copy(0.4f),
                        style = VolunteersCaseTheme.typography.labelLarge
                    )
                }
            },
            title = {
                Text(
                    text = "Выберите время",
                    style = VolunteersCaseTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Abyss,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = Graphite.copy(alpha = 0.03f),
                            clockDialSelectedContentColor = Color.White,
                            clockDialUnselectedContentColor = Void,
                            selectorColor = Lagoon,
                            timeSelectorSelectedContainerColor = Lagoon.copy(alpha = 0.08f),
                            timeSelectorSelectedContentColor = Lagoon,
                            timeSelectorUnselectedContainerColor = Graphite.copy(alpha = 0.04f),
                            timeSelectorUnselectedContentColor = Graphite,
                            periodSelectorSelectedContainerColor = Mint.copy(alpha = 0.2f),
                            periodSelectorSelectedContentColor = Abyss,
                            periodSelectorUnselectedContainerColor = Color.Transparent,
                            periodSelectorUnselectedContentColor = Graphite
                        )
                    )
                }
            },
            containerColor = Milk,
            shape = RoundedCornerShape(24.dp)
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Milk,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        topBar = {
            SyncedTopNavigationBarCoordinator(
                pagerState = pagerState,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        val _unusedPadding = paddingValues

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds(),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = onRefreshAction,
                        modifier = Modifier.fillMaxSize(),
                        indicator = {
                            PullToRefreshDefaults.Indicator(
                                state = rememberPullToRefreshState(),
                                isRefreshing = isRefreshing,
                                containerColor = Arctic,
                                color = Lagoon,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 80.dp) // Чуть опустили индикатор ниже топбара
                            )
                        }
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            // КРИТИЧЕСКИ ВАЖНО: Разрешаем сетке рисовать элементы за пределами своих границ (под топ-баром)
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 90.dp, // Делаем базовый отступ чуть больше, чтобы в покое всё стояло идеально под топ-баром
                                bottom = 24.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (uiState.eventsLoading) {
                                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Lagoon)
                                    }
                                } else if (uiState.myEvents.isNotEmpty()) {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        uiState.myEvents.take(3).forEach { event ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                tonalElevation = 1.dp,
                                                color = Arctic
                                            ) {
                                                EventSummaryCard(event = event) {
                                                    onLoadApplications(event.eventId)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        "Пока пусто...",
                                        style = VolunteersCaseTheme.typography.titleMedium,
                                        color = Silver,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }

                                if (uiState.applications.isNotEmpty()) {
                                    Spacer(Modifier.height(28.dp))
                                    Text(
                                        "Заявки на участие",
                                        style = VolunteersCaseTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Abyss,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        uiState.applications.forEach { app ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                tonalElevation = 1.dp,
                                                color = Arctic
                                            ) {
                                                ApplicationCard(
                                                    app = app,
                                                    onApprove = { onApprove(app.eventId.toLong(), app.userId.toLong()) },
                                                    onReject = { onReject(app.eventId.toLong(), app.userId.toLong()) }
                                                )
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            // Сдвигаем форму на 90.dp сверху, чтобы она не залезала под статичный бар в начале
                            .padding(start = 20.dp, end = 20.dp, top = 90.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            tonalElevation = 1.dp,
                            color = Arctic
                        ) {
                            Column(
                                Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    "Основная информация",
                                    style = VolunteersCaseTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Abyss
                                )

                                CustomTextField(Modifier.fillMaxWidth(), "Название") { updateFields(fields.copy(name = it)) }
                                CustomTextField(Modifier.fillMaxWidth(), "Описание") { updateFields(fields.copy(description = it)) }
                                CustomTextField(Modifier.fillMaxWidth(), "Макс. участников") { updateFields(fields.copy(maxCapacity = it.toLongOrNull() ?: 0)) }
                                CustomTextField(Modifier.fillMaxWidth(), "ID Тегов (через запятую)") {
                                    val ids = it.split(",").mapNotNull { id -> id.trim().toLongOrNull() }
                                    updateFields(fields.copy(tagIds = ids))
                                }

                                HorizontalDivider(color = Milk, thickness = 1.dp)

                                Text(
                                    "Дата и медиа",
                                    style = VolunteersCaseTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Abyss
                                )

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Milk,
                                    border = BorderStroke(1.dp, Graphite.copy(alpha = 0.08f)),
                                    onClick = { setShowDatePicker(true) }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            androidx.compose.material3.Icon(
                                                imageVector = androidx.compose.material.icons.Icons.Outlined.CalendarMonth,
                                                contentDescription = null,
                                                tint = Graphite.copy(alpha = 0.6f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(Modifier.width(12.dp))
                                            Text(
                                                text = if (fields.selectedDateTime != null) "Дата проведения" else "Укажите дату и время",
                                                style = VolunteersCaseTheme.typography.titleMedium,
                                                color = if (fields.selectedDateTime != null) Void else Graphite.copy(alpha = 0.5f),
                                                fontSize = 15.sp
                                            )
                                        }

                                        fields.selectedDateTime?.let { dt ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Lagoon.copy(alpha = 0.08f),
                                                modifier = Modifier.padding(start = 8.dp)
                                            ) {
                                                Text(
                                                    text = dt.format(visualFormatter),
                                                    color = Lagoon,
                                                    style = VolunteersCaseTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                val hasCover = uiState.selectedCover != null
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (hasCover) Mint.copy(alpha = 0.06f) else Graphite.copy(alpha = 0.02f),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (hasCover) Mint.copy(alpha = 0.4f) else Graphite.copy(alpha = 0.1f)
                                    ),
                                    onClick = { if (!uiState.isUploadingCover) launcher.launch("image/*") }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            androidx.compose.material3.Icon(
                                                imageVector = if (hasCover) androidx.compose.material.icons.Icons.Outlined.CheckCircle else androidx.compose.material.icons.Icons.Outlined.Image,
                                                contentDescription = null,
                                                tint = if (hasCover) Mint else Graphite.copy(alpha = 0.6f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(Modifier.width(12.dp))
                                            Text(
                                                text = if (hasCover) "Обложка успешно добавлена" else "Загрузить фоновое изображение",
                                                style = VolunteersCaseTheme.typography.titleMedium,
                                                color = if (hasCover) Abyss else Graphite.copy(alpha = 0.5f),
                                                fontSize = 15.sp
                                            )
                                        }

                                        if (uiState.isUploadingCover) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                color = Lagoon,
                                                strokeWidth = 2.dp
                                            )
                                        } else if (hasCover) {
                                            Text(
                                                text = "Изменить",
                                                color = Graphite.copy(alpha = 0.6f),
                                                style = VolunteersCaseTheme.typography.labelMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.clickable { launcher.launch("image/*") }
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = Milk, thickness = 1.dp)

                                Text(
                                    "Локация проведения",
                                    style = VolunteersCaseTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Abyss
                                )

                                CustomSearchTextField(
                                    label = "Введите адрес для поиска",
                                    value = "",
                                    onConfirm = { onSearchLocation(it) },
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (uiState.isSearchMode) {
                                    if (uiState.searchLoading) {
                                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Lagoon)
                                        }
                                    } else {
                                        Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                                            uiState.searchResults.forEach { location ->
                                                Text(
                                                    location.address,
                                                    style = VolunteersCaseTheme.typography.labelSmall,
                                                    color = Graphite,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable { onSelectLocation(location) }
                                                        .padding(vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                uiState.selectedLocation?.let {
                                    Surface(
                                        color = Mint.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Выбрано: ${it.address}",
                                            color = Abyss,
                                            style = VolunteersCaseTheme.typography.labelMedium,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                CustomButton(
                                    text = "Опубликовать",
                                    enabled = fields.name.isNotBlank() &&
                                            uiState.selectedLocation != null &&
                                            uiState.selectedCover != null &&
                                            fields.selectedDateTime != null,
                                    isLoading = uiState.isLoading,
                                    onClick = createAction,
                                    variant = ButtonVariant.Wide,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

object CoordinatorFileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            file
        } catch (e: java.io.IOException) {
            android.util.Log.e("CoordinatorFileUtils", "I/O error while reading uri=$uri", e)
            null
        } catch (e: SecurityException) {
            android.util.Log.e("CoordinatorFileUtils", "Permission denied for uri=$uri", e)
            null
        }
    }
}

@Preview
@Composable
private fun CoordinatorScreenPreview() {
    VolunteersCaseTheme {
        CoordinatorScreenContent(
            animationOverride = true, uiState = CoordinatorState(
                isLoading = false, applications = listOf(
                    EventApplication()
                )
            )
        )
    }
}