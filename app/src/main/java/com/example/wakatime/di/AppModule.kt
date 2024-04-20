package com.example.wakatime.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.wakatime.data.remote.WakaApi
import com.example.wakatime.data.remote.WakaInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideWeatherApi(wakaInterceptor: WakaInterceptor, @ApplicationContext context: Context): WakaApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(wakaInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
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