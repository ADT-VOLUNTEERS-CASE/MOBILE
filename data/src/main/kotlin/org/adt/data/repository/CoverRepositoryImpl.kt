package org.adt.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.Json
import org.adt.core.annotations.RepositoryImpl
import org.adt.core.entities.GeneralResponse
import org.adt.core.entities.event.Cover
import org.adt.core.entities.response.ErrorResponse
import org.adt.data.abstraction.PersistenceRepository
import org.adt.domain.abstraction.CoverRepository
import java.io.File
import java.net.URLConnection
import javax.inject.Inject

@RepositoryImpl(suppressed = true)
class CoverRepositoryImpl @Inject constructor(
    private val networkRepository: KtorRepository,
    private val persistenceRepository: PersistenceRepository,
) : CoverRepository {
    private val json = Json { ignoreUnknownKeys = true }

    private fun parseError(bodyAsText: String?): ErrorResponse? {
        return try {
            bodyAsText?.let { json.decodeFromString<ErrorResponse>(it) }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun uploadCover(file: File, retried: Boolean): GeneralResponse<Cover> {
        val token = persistenceRepository.getToken() ?: throw Exception("Not authorized")

        val filePart = createCoverFile(file)

        val response = networkRepository.uploadCover(token, filePart)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.body()!!)
        }

        /*
        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return uploadCover(file, retried = true)
            }

            val error = parseError(response.bodyAsText())
            persistenceRepository.removeToken()

            return GeneralResponse.failure(403, error?.message ?: "HTTP ${response.status.value}")
        }
         */

        return GeneralResponse.failure(response.status.value, "HTTP ${response.status.value}")
    }

    override suspend fun deleteCover(
        coverId: Long,
        retried: Boolean
    ): GeneralResponse<Int> {
        val token = persistenceRepository.getToken() ?: return GeneralResponse.failure(
            401,
            "Not authorized"
        )
        val response = networkRepository.deleteCover(token, coverId)

        if (response.status.isSuccess()) {
            return GeneralResponse.success(response.status.value)
        }

        /*
        if (response.status.value == 403 && !retried) {
            val refresh = requestFreshAccessToken()
            if (refresh.isSuccessful) {
                return deleteCover(coverId, retried = true)
            }
            persistenceRepository.removeToken()
        }
         */

        val error = parseError(response.bodyAsText())
        return GeneralResponse.failure(
            response.status.value,
            error?.message ?: "HTTP ${response.status.value}"
        )
    }
}

internal fun createCoverFile(file: File): MultiPartFormDataContent {
    val contentType = file.inputStream().buffered().use { URLConnection.guessContentTypeFromStream(it) }
        ?: URLConnection.guessContentTypeFromName(file.name)
        ?: "image/*"
    return MultiPartFormDataContent(formData {
        val headers = Headers.build {
            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
            append(HttpHeaders.ContentType, contentType)
        }
        appendInput("file", headers, file.length()) { file.inputStream().asSource().buffered() }
    })
}
