package com.mgkim.sample.di.module

import com.mgkim.sample.network.api.kakao.KSearchApi
import com.mgkim.sample.constants.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RestfulModule {

    @Provides
    @Singleton
    fun provideKSearchApi(
        @Named("authorized") okHttpClient: OkHttpClient): KSearchApi = getRetrofitBuilder(okHttpClient).create(
        KSearchApi::class.java)

    private fun getRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun getRetrofitBuilder(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}