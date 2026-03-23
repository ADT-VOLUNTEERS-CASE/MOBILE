package org.adt.data.abstraction

interface NetworkStatusProvider {
    fun isInternetAvailable(): Boolean
}