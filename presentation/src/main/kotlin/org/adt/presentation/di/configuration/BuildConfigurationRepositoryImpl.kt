package org.adt.presentation.di.configuration

import jakarta.inject.Inject
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import org.adt.presentation.BuildConfig

@ImplicitUsage
class BuildConfigurationRepositoryImpl @Inject constructor(
    override val isDebug: Boolean = false
) : BuildConfigurationRepository {
    override fun getApiBaseUrl(): String {
        return BuildConfig.API_BASE_URL
    }
}