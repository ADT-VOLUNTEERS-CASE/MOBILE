package org.adt.data.repository

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import org.adt.core.entities.request.RefreshRequest

interface AuthKtorRepository {
    /**
     * SUCCESS:
     *      200 | OK
     * ERRORS:
     *      400 | Invalid data
     *      403 | Forbidden (Expired Token)
     */
    @POST("v1/auth/refreshtoken")
    suspend fun refreshToken(
        @Body request: RefreshRequest
    ): HttpResponse
}