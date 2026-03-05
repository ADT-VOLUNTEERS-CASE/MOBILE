package org.adt.data.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import org.adt.data.abstraction.INetworkRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
internal object NetworkModule {
    @Provides
    @ImplicitUsage
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://adt.rss14.ru/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @ImplicitUsage
    fun provideNetworkRepository(retrofit: Retrofit): INetworkRepository {
        return retrofit.create(INetworkRepository::class.java)
    }
}