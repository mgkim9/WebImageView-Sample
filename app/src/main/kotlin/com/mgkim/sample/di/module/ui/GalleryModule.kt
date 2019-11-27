package com.mgkim.sample.di.module.ui

import androidx.lifecycle.ViewModelProviders
import com.mgkim.sample.ui.base.ViewModelProviderFactory
import com.mgkim.sample.ui.gallery.view.GalleryFragment
import com.mgkim.sample.ui.gallery.viewmodel.GalleryViewModel
import dagger.Module
import dagger.Provides

@Module
class GalleryModule {
    @Provides
    fun provideGalleryViewModel(fragment: GalleryFragment): GalleryViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(GalleryViewModel()))[GalleryViewModel::class.java]
}