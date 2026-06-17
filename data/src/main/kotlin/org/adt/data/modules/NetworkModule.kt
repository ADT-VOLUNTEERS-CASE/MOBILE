package org.adt.data.modules

import dagger.Module
import dagger.Provides
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.NetworkStatusProvider
import org.adt.data.repository.KtorRepository
import org.adt.data.repository.createKtorRepository
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Module
internal object NetworkModule {
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

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
        offlinePlugin: io.ktor.client.plugins.api.ClientPlugin<Unit>,
        networkStatusProvider: NetworkStatusProvider
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
                header(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }

            install(ContentNegotiation) {
                json(jsonConfig)
            }

            install(offlinePlugin)

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