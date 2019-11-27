package com.mgkim.sample.di.component

import android.app.Application
import com.mgkim.sample.di.module.*
import com.mgkim.sample.SampleTestApp
import com.mgkim.sample.di.module.ActivityBindModule
import com.mgkim.sample.di.module.NetworkModule
import com.mgkim.sample.di.module.RepositoryModule
import com.mgkim.sample.di.module.RestfulModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        RestfulModule::class,
        RepositoryModule::class,
        ActivityBindModule::class]
)
interface AppComponent : AndroidInjector<SampleTestApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}