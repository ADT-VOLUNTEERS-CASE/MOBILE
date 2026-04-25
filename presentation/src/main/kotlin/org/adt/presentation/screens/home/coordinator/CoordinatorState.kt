package org.adt.presentation.screens.home.coordinator

import org.adt.core.entities.Location
import org.adt.core.entities.event.Cover

data class CoordinatorState(
    val isLoading: Boolean = false,
    val createError: String? = null,
    // Поиск локации
    val isSearchMode: Boolean = false,
    val searchLoading: Boolean = false,
    val searchResults: List<Location> = listOf(),
    val selectedLocation: Location? = null,
    // Обложка
    val selectedCover: Cover? = null,
    val isUploadingCover: Boolean = false,
)