package org.adt.data.mocks

import org.adt.core.entities.UserRole

data class MockUserModel(
    val email: String = "", val password: String = "", val role: UserRole = UserRole.VOLUNTEER
)