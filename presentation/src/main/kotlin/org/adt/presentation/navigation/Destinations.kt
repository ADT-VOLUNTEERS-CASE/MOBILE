package org.adt.presentation.navigation

import kotlinx.serialization.Serializable
import org.adt.core.entities.UserRole
import org.adt.core.entities.event.Event

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

    @Serializable
    data object VolunteerProfile: Destinations()

    @Serializable
    data object VolunteerCalendar : Destinations()

    @Serializable
    data object VolunteerStatistics : Destinations()

    @Serializable
    data class EventDetails(val id: Long) : Destinations()

    companion object {
        fun mapRole(userRole: UserRole): Destinations {
            return when (userRole) {
                UserRole.ADMIN -> AdminHome
                UserRole.COORDINATOR -> CoordinatorHome
                UserRole.VOLUNTEER -> VolunteerHome
                else -> Authenticate
            }
        }
    }
}