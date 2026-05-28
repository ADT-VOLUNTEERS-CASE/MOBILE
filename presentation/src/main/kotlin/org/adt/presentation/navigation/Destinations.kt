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
    data object AdminDashboard : Destinations()

    @Serializable
    data object AdminSystemTools : Destinations()


    @Serializable
    data object CoordinatorHome : Destinations()

    @Serializable
    data object CoordinatorReport : Destinations()

    @Serializable
    data object CoordinatorProfile : Destinations()

    @Serializable
    data object VolunteerHome : Destinations()

    @Serializable
    data object VolunteerProfile: Destinations()

    @Serializable
    data object VolunteerCalendar : Destinations()

    @Serializable
    data object VolunteerStatistics : Destinations()

    @Serializable
    data object VolunteerRating : Destinations()

    @Serializable
    data class EventDetails(val id: Long) : Destinations()

    @Serializable
    data object NoConnectionScreen: Destinations()

    companion object {
        fun mapRole(userRole: UserRole): Destinations {
            return when (userRole) {
                UserRole.ADMIN -> AdminDashboard
                UserRole.COORDINATOR -> CoordinatorHome
                UserRole.VOLUNTEER -> VolunteerHome
                else -> Authenticate
            }
        }
    }
}