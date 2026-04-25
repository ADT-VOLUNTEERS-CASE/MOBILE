package org.adt.presentation.screens.home.coordinator

// УДАЛИТЕ импорт android.os.FileUtils
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import org.adt.presentation.components.buttons.CustomTranslucentButton
import org.adt.presentation.navigation.Destinations
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.VolunteersCaseTheme
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

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
        }
    )
}

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
    animationOverride: Boolean = false
) {
    val context = LocalContext.current
    val visualFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

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

    Box(
        Modifier
            .fillMaxSize()
            .background(Abyss)
            .padding(vertical = 15.dp)
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
                Modifier,
                text = "Твоё следующее доброе дело ждёт своего момента",
                charDelay = if (animationOverride) 0L else 40L,
                animationOverride = animationOverride
            )

            Column(
                Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VolunteersCaseTheme.colors.secondaryBackground)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                Text("Создание\nмероприятия", style = VolunteersCaseTheme.typography.titleLarge.copy(textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())

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
                    onClick = {
                        // Вызываем встроенный Android UI
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(context, { _, year, month, day ->
                            TimePickerDialog(context, { _, hour, minute ->
                                val dt = LocalDateTime.of(year, month + 1, day, hour, minute)
                                updateFields(fields.copy(
                                    selectedDateTime = dt,
                                    dateTimestamp = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                                ))
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                    },
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
                        CircularProgressIndicator(Modifier.padding(8.dp))
                    } else {
                        uiState.searchResults.forEach { location ->
                            TextButton(onClick = { onSelectLocation(location) }) {
                                Text(location.address, color = Graphite)
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

                Spacer(Modifier.height(32.dp))

                CustomButton (
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

            TextButton(logoutAction, contentPadding = PaddingValues(2.dp)) {
                Text(
                    "Выйти",
                    style = VolunteersCaseTheme.typography.titleMedium.copy(
                        Arctic,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomBottomBar(
                Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 15.dp),
                UserRole.COORDINATOR, Destinations.CoordinatorHome, { }
            )
        }
    }
}

object CoordinatorFileUtils {
    fun getFileFromUri(context: android.content.Context, uri: android.net.Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { inputStream.copyTo(it) }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun showDateTimePicker(
    context: android.content.Context,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, day ->
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    val selectedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                    onDateTimeSelected(selectedDateTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}


@Preview
@Composable
private fun CoordinatorScreenPreview() {
    VolunteersCaseTheme {
        CoordinatorScreenContent(animationOverride = true)
    }
}