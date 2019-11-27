package com.mgkim.sample.di.module.ui

import androidx.lifecycle.ViewModelProviders
import com.mgkim.sample.ui.base.ViewModelProviderFactory
import com.mgkim.sample.ui.home.model.HomeModel
import com.mgkim.sample.ui.home.view.HomeFragment
import com.mgkim.sample.ui.home.view.MainActivity
import com.mgkim.sample.ui.home.viewmodel.HomeViewModel
import com.mgkim.sample.ui.home.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {
    @Provides
    fun provideMainViewModel(activity: MainActivity): MainViewModel = ViewModelProviders.of(activity, ViewModelProviderFactory(MainViewModel()))[MainViewModel::class.java]

    @Provides
    fun provideHomeViewModel(fragment : HomeFragment, model:HomeModel): HomeViewModel = ViewModelProviders.of(fragment, ViewModelProviderFactory(HomeViewModel(model)))[HomeViewModel::class.java]

    @Provides
    fun provideHomeModel(): HomeModel = HomeModel()

}