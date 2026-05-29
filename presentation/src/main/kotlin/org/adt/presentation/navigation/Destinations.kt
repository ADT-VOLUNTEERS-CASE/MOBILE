package org.adt.presentation.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import org.adt.core.entities.UserRole

@Keep
@Serializable
sealed class Destinations {
    @Keep
    @Serializable
    data object Splash : Destinations()

    @Keep
    @Serializable
    data object Home : Destinations()

    @Keep
    @Serializable
    data object Authenticate : Destinations()

    @Keep
    @Serializable
    data object Register : Destinations()

    @Keep
    @Serializable
    data object AdminRegister : Destinations()

    @Keep
    @Serializable
    data object AdminDashboard : Destinations()

    @Keep
    @Serializable
    data object AdminHome : Destinations()

    @Keep
    @Serializable
    data object AdminUsers : Destinations()

    @Keep
    @Serializable
    data object AdminSystemTools : Destinations()

    @Keep
    @Serializable
    data object CoordinatorHome : Destinations()

    @Keep
    @Serializable
    data object CoordinatorReport : Destinations()

    @Keep
    @Serializable
    data object CoordinatorProfile : Destinations()

    @Keep
    @Serializable
    data object VolunteerHome : Destinations()

    @Keep
    @Serializable
    data object VolunteerProfile: Destinations()

    @Keep
    @Serializable
    data object VolunteerCalendar : Destinations()

    @Keep
    @Serializable
    data object VolunteerStatistics : Destinations()

    @Keep
    @Serializable
    data object VolunteerRating : Destinations()

    @Keep
    @Serializable
    data class EventDetails(val id: Long) : Destinations()

    @Keep
    @Serializable
    data object NoConnectionScreen: Destinations()

    @Keep
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