package com.example.wakatime.di

import com.example.wakatime.data.remote.WakaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WakaApi {
        return Retrofit.Builder()
            .baseUrl("https://wakatime.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WakaApi::class.java)
    }
}