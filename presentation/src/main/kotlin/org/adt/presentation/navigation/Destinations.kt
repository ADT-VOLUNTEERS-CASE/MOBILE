package org.adt.presentation.navigation

import kotlinx.serialization.Serializable
import org.adt.core.entities.UserRole

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

    companion object {
        fun mapRole(userRole: UserRole): Destinations {
            return when (userRole) {
                UserRole.ADMIN -> Destinations.AdminHome
                UserRole.COORDINATOR -> Destinations.CoordinatorHome
                UserRole.VOLUNTEER -> Destinations.VolunteerHome
                else -> Destinations.Authenticate
            }
        }
    }
}