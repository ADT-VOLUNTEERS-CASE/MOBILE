package org.adt.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations {

    @Serializable
    data object Splash : Destinations()

    @Serializable
    data object Home : Destinations()

    @Serializable
    data object Authenticate : Destinations()

    @Serializable
    data object Register : Destinations()

    @Serializable
    data object AdminRegister : Destinations()

    @Serializable
    data object AdminHome : Destinations()

    @Serializable
    data object CoordinatorHome : Destinations()

    @Serializable
    data object VolunteerHome : Destinations()
}