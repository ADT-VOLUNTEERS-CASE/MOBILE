package org.adt.data.modules

import dagger.Module
import dagger.Provides
import org.adt.core.annotations.ImplicitUsage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal object NetworkModule {
    @Provides
    @ImplicitUsage
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://adt.rss14.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}