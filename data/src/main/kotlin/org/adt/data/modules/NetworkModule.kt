package org.adt.data.modules

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.IConfigRepository
import org.adt.data.abstraction.INetworkRepository
import org.adt.data.abstraction.INetworkStatusProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal object NetworkModule {
    @Provides
    @Singleton
    @ImplicitUsage
    fun provideInterceptor(
        networkStatusProvider: INetworkStatusProvider
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
        configRepository: IConfigRepository
    ): Retrofit {
        val url = configRepository.getApiBaseUrl()
        return Retrofit.Builder()
            .baseUrl(url) //"https://adt.rss14.ru/api/v1/"
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @ImplicitUsage
    fun provideNetworkRepository(retrofit: Retrofit): INetworkRepository{
        return retrofit.create(INetworkRepository::class.java)
    }
}