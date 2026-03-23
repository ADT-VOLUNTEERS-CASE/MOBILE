package org.adt.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations {

    @Serializable
    data object Splash : Destinations()

    @Serializable
    data object Home : Destinations()

}