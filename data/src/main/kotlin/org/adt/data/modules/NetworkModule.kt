package org.adt.data.modules

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.INetworkRepository
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
    fun provideInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
                return chain.proceed(request)
            }
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://adt.rss14.ru/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @ImplicitUsage
    fun provideNetworkRepository(retrofit: Retrofit): INetworkRepository {
        return retrofit.create(INetworkRepository::class.java)
    }
}