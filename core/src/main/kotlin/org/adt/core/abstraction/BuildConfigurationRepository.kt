package org.adt.core.abstraction

interface BuildConfigurationRepository {
    val isDebug: Boolean
    fun getApiBaseUrl(): String
}