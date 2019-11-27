package com.mgkim.sample.di.module

import com.mgkim.sample.constants.KAKAO_REST_API_KEY
import com.mgkim.sample.repository.preferences.AppPreference
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {
    @Provides
    @Named("unauthorized")
    @Singleton
    fun provideUnauthorizedOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            requestInterceptor: RequestInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Named("authorized")
    @Singleton
    fun provideAuthorizedOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authRequestInterceptor: AuthRequestInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(authRequestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        HttpLoggingInterceptor.Level.BODY
//        level = if(IS_DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideRequestInterceptor(preference: AppPreference): RequestInterceptor = RequestInterceptor(preference)

    @Provides
    @Singleton
    fun provideAuthRequestInterceptor(preference: AppPreference): AuthRequestInterceptor = AuthRequestInterceptor(preference)

    class RequestInterceptor(private val preference: AppPreference) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val request = request().newBuilder().apply {
                addHeader("Service", "android")
            }.build()
            proceed(request)
        }
    }

    class AuthRequestInterceptor(private val preference: AppPreference) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val request = request().newBuilder().apply {
                addHeader("Authorization", "KakaoAK $KAKAO_REST_API_KEY")
                addHeader("Service", "android")
            }.build()
            proceed(request)
        }
    }
}