package org.adt.data.modules

import dagger.Module
import dagger.Provides
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import org.adt.core.entities.request.RefreshRequest
import org.adt.core.entities.response.AuthResponse
import org.adt.data.abstraction.NetworkStatusProvider
import org.adt.data.abstraction.PersistenceRepository
import org.adt.data.repository.KtorRepository
import org.adt.data.repository.createAuthKtorRepository
import org.adt.data.repository.createKtorRepository
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Module
internal object NetworkModule {
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val tokenMutex = Mutex()

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideOfflineAndExceptionPlugin(
        networkStatusProvider: NetworkStatusProvider
    ) = createClientPlugin("OfflineAndExceptionPlugin") {
        onRequest { request, _ ->
            if (!networkStatusProvider.isInternetAvailable()) {
                throw CancellationException("No internet connection")
            }
        }

        onResponse { response ->
            // Handle global status codes
        }
    }

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideHttpClient(
        offlinePlugin: ClientPlugin<Unit>,
        buildConfigurationRepository: BuildConfigurationRepository,
        tokenRepository: PersistenceRepository,
    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    retryOnConnectionFailure(true)
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }

            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }

            install(ContentNegotiation) {
                json(jsonConfig)
            }

            install(offlinePlugin)

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenRepository.getToken() ?: return@loadTokens null
                        val refreshToken =
                            tokenRepository.getRefreshToken() ?: return@loadTokens null
                        BearerTokens(accessToken, refreshToken)
                    }

                    refreshTokens {
                        tokenMutex.withLock {
                            Dispatchers.IO {
                                val isolatedClient = HttpClient(OkHttp) {
                                    install(ContentNegotiation) { json(jsonConfig) }
                                    install(DefaultRequest) {
                                        contentType(ContentType.Application.Json)
                                        header(
                                            HttpHeaders.Accept,
                                            ContentType.Application.Json.toString()
                                        )
                                    }
                                }

                                val authKtorfit = provideKtorfit(
                                    isolatedClient,
                                    buildConfigurationRepository
                                ).createAuthKtorRepository()

                                val currentRefreshToken = tokenRepository.getRefreshToken()

                                if (currentRefreshToken.isNullOrBlank())
                                    return@IO null

                                val request = RefreshRequest(currentRefreshToken)
                                val response = authKtorfit.refreshToken(request)

                                if (!response.status.isSuccess()) {
                                    tokenRepository.removeToken()
                                    tokenRepository.removeRefreshToken()
                                    return@IO null
                                }

                                val data = response.body<AuthResponse>()

                                val newAccess = data.accessToken
                                val newRefresh = data.refreshToken

                                tokenRepository.saveTokens(newAccess, newRefresh)
                                BearerTokens(newAccess, newRefresh)
                            }
                        }
                    }

                    // Restrict refresh requests to foreign endpoints
                    sendWithoutRequest { request ->
                        request.url.buildString()
                            .contains(buildConfigurationRepository.getApiBaseUrl())
                    }
                }
            }

            expectSuccess = false
        }
    }

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideKtorfit(
        httpClient: HttpClient,
        buildConfigurationRepository: BuildConfigurationRepository
    ): Ktorfit {
        val url = buildConfigurationRepository.getApiBaseUrl()

        return Ktorfit.Builder()
            .baseUrl(url)
            .httpClient(httpClient)
            .build()
    }

    @Provides
    @ImplicitUsage
    fun provideNetworkRepository(ktorfit: Ktorfit): KtorRepository {
        return ktorfit.createKtorRepository()
    }
}