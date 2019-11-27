package com.mgkim.sample.di.module.ui

import androidx.lifecycle.ViewModelProviders
import com.mgkim.sample.ui.base.ViewModelProviderFactory
import com.mgkim.sample.ui.slideshow.model.SlideshowViewModel
import com.mgkim.sample.ui.slideshow.view.SlideshowFragment
import dagger.Module
import dagger.Provides

@Module
class SlideshowModule {

    @Provides
    fun provideSlideshowViewModel(fragment: SlideshowFragment): SlideshowViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(SlideshowViewModel()))[SlideshowViewModel::class.java]
}