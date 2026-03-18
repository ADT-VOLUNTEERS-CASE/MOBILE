package org.adt.data.abstraction

interface INetworkStatusProvider {
    fun isInternetAvailable(): Boolean
}