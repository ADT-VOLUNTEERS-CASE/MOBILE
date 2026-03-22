package org.adt.data.modules

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.abstraction.BuildConfigurationRepository
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.repository.RetrofitRepository
import org.adt.data.abstraction.NetworkStatusProvider
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal object NetworkModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideInterceptor(
        networkStatusProvider: NetworkStatusProvider
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            if (!networkStatusProvider.isInternetAvailable()) {
                val emptyBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())

                return@Interceptor okhttp3.Response.Builder()
                    .request(request)
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(503)
                    .message("No internet connection")
                    .body(emptyBody)
                    .build()
            }
            val newRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideOkHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @ImplicitUsage
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        buildConfigurationRepository: BuildConfigurationRepository
    ): Retrofit {
        val url = buildConfigurationRepository.getApiBaseUrl()
        return Retrofit.Builder()
            .baseUrl(url) //"https://adt.rss14.ru/api/v1/"
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=utf-8".toMediaType())
            ).build()
    }

    @Provides
    @ImplicitUsage
    fun provideNetworkRepository(retrofit: Retrofit): RetrofitRepository {
        return retrofit.create(RetrofitRepository::class.java)
    }
}