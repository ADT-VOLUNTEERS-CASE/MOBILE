package org.adt.presentation.screens.home.admin.tools

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adt.presentation.R
import org.adt.presentation.components.textfields.CustomTextField
import org.adt.presentation.components.buttons.ButtonColorScheme
import org.adt.presentation.components.buttons.ButtonStyle
import org.adt.presentation.components.buttons.CustomButton
import org.adt.presentation.theme.Abyss
import org.adt.presentation.theme.Arctic
import org.adt.presentation.theme.Graphite
import org.adt.presentation.theme.Milk
import org.adt.presentation.theme.VolunteersCaseTheme

@Composable
fun AdminSystemToolsScreen(
    viewModel: AdminSystemToolsViewModel
) {
    val uiState = viewModel.toolsState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToolsToast()
        }
    }

    AdminSystemToolsContent(
        uiState = uiState,
        onTagInputChange = { viewModel.onTagInputChange(it) },
        onTagAction = { action ->
            when (action) {
                "create" -> viewModel.createTag()
                "info" -> viewModel.getTagInfo()
                "delete" -> viewModel.deleteTag()
            }
        },
        onEventIdChange = { viewModel.onDeleteEventIdChange(it) },
        onDeleteEvent = { viewModel.deleteEvent() },
        onCoverIdChange = { viewModel.onDeleteCoverIdChange(it) },
        onDeleteCover = { viewModel.deleteCover() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSystemToolsContent(
    uiState: AdminSystemToolsState = AdminSystemToolsState(),
    onTagInputChange: (String) -> Unit = {},
    onTagAction: (String) -> Unit = {},
    onEventIdChange: (String) -> Unit = {},
    onDeleteEvent: () -> Unit = {},
    onCoverIdChange: (String) -> Unit = {},
    onDeleteCover: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Milk
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)) {
                Text(
                    text = stringResource(R.string.title_system_utils),
                    style = VolunteersCaseTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Abyss,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = stringResource(R.string.label_moderation_tools),
                    style = VolunteersCaseTheme.typography.labelMedium,
                    color = Graphite.copy(alpha = 0.7f)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Arctic,
                border = BorderStroke(1.dp, Graphite.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.subtitle_tag_manage),
                        style = VolunteersCaseTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Abyss
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.textfield_tag_name),
                        value = uiState.tagInput,
                        onValueChange = onTagInputChange
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.button_create),
                            onClick = { onTagAction("create") }
                        )
                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.button_get_id),
                            onClick = { onTagAction("info") },
                            style = ButtonStyle.Outlined
                        )
                    }

                    CustomButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.button_delete_tag),
                        onClick = { onTagAction("delete") },
                        style = ButtonStyle.Outlined,
                        colors = ButtonColorScheme(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFD32F2F).copy(alpha = 0.8f),
                            borderColor = Color(0xFFD32F2F).copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Arctic,
                border = BorderStroke(1.dp, Graphite.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.subtitle_extra_delete),
                        style = VolunteersCaseTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Abyss
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.textfield_event_id),
                            value = uiState.deleteEventId,
                            onValueChange = onEventIdChange
                        )
                        CustomButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.button_delete_event),
                            onClick = { onDeleteEvent() },
                            style = ButtonStyle.Outlined,
                            colors = ButtonColorScheme(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFFD32F2F).copy(alpha = 0.8f),
                                borderColor = Color(0xFFD32F2F).copy(alpha = 0.2f)
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Milk)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CustomTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.button_cover_id),
                            value = uiState.deleteCoverId,
                            onValueChange = onCoverIdChange
                        )
                        CustomButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.button_delete_cover),
                            onClick = { onDeleteCover() },
                            style = ButtonStyle.Outlined,
                            colors = ButtonColorScheme(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFFD32F2F).copy(alpha = 0.8f),
                                borderColor = Color(0xFFD32F2F).copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AdminSystemToolsPreview() {
    VolunteersCaseTheme {
        AdminSystemToolsContent()
    }
}