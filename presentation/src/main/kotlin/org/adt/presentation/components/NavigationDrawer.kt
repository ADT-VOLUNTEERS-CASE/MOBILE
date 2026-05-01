package org.adt.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.adt.presentation.theme.VolunteersCaseTheme
import kotlin.math.abs

@Composable
fun NavigationDrawer(
    drawerState: DrawerState, screenContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val density = LocalDensity.current
    val drawerWidthPx = with(density) { 360.dp.toPx() }

    val closeMenuIconMirrorScale by remember {
        derivedStateOf {
            if (drawerState.currentOffset.isNaN()) 1f
            else {
                (1 - (abs(drawerState.currentOffset) / drawerWidthPx) * 4).coerceIn(-1f,1f)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(drawerState = drawerState) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Центр Городских Волонтёров",
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            style = VolunteersCaseTheme.typography.titleLarge
                        )
                        IconButton(onClick = { //TODO
                            scope.launch {
                                drawerState.close()
                            }
                        }) {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .scale(closeMenuIconMirrorScale, 1f)
                                ,
                                imageVector = Icons.AutoMirrored.Outlined.MenuOpen,
                                contentDescription = "Close menu"
                            )
                        }

                    }
                    HorizontalDivider()
                    Column(modifier = Modifier.weight(3f)) {
                        Text(
                            "Section 1",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Item 1") },
                            selected = false,
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Item 2") },
                            selected = false,
                            onClick = { /* Handle click */ })
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Section 2",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = false,
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Settings"
                                )
                            },
                            onClick = { /* Handle click */ })
                        NavigationDrawerItem(
                            label = { Text("Info") },
                            selected = false,
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Help,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Info"
                                )
                            },
                            onClick = { /* Handle click */ },
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        },
        gesturesEnabled = true
    ) {
        screenContent()
    }
}

@Preview
@Composable
private fun NavigationDrawerPreview() {
    VolunteersCaseTheme {
        NavigationDrawer(rememberDrawerState(DrawerValue.Open)) { }
    }
}