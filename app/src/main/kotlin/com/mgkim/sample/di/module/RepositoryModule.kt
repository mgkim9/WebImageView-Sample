package com.mgkim.sample.di.module

import android.app.Application
import com.mgkim.sample.repository.preferences.AppPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAppPreference(application: Application): AppPreference = AppPreference(application.applicationContext)
}