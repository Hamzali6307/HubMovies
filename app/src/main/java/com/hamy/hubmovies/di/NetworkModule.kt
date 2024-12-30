package com.hamy.hubmovies.di

import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.data.network.ApiService.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi() :Moshi = Moshi.Builder().run {
        add(KotlinJsonAdapterFactory())
        build()
    }
    @Provides
    @Singleton
    fun provideApiService(moshi: Moshi) : ApiService = Retrofit.Builder()
        .run {
            baseUrl(BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            build()
        }.create(ApiService::class.java)

}