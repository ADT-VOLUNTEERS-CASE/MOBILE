package org.adt.storage.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.adt.data.abstraction.PersistenceRepository
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("token_prefs")

@Singleton
class PersistenceRepositoryImpl @Inject constructor(
    @param: ApplicationContext private val context: Context
) : PersistenceRepository {
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("accessToken")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_TOKEN]?.let { "Bearer $it" }
    }

    override suspend fun saveToken(token: String) {
        Log.d("TokenHelper", "Saving token: $token")
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
        }
    }

    override suspend fun getToken(): String? {
        val tokenValue = context.dataStore.data
            .map { prefs -> prefs[KEY_TOKEN] }
            .firstOrNull()
        val bearerToken = tokenValue?.let { "Bearer $it" }
        Log.d("TokenHelper", "Getting token: $bearerToken")
        return bearerToken
    }

    override suspend fun removeToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_TOKEN)
        }
    }
}