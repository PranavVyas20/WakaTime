package com.example.wakatime.di

import com.example.wakatime.data.remote.WakaApi
import com.example.wakatime.data.remote.WakaInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(wakaInterceptor: WakaInterceptor): WakaApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(wakaInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://wakatime.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WakaApi::class.java)
    }
}