package com.mgkim.sample.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Named("applicationContext")
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext
}