package com.mgkim.sample.di.module

import com.mgkim.sample.di.module.ui.GalleryModule
import com.mgkim.sample.di.module.ui.HomeModule
import com.mgkim.sample.di.module.ui.SearchImageModule
import com.mgkim.sample.ui.base.view.BaseActivity
import com.mgkim.sample.ui.gallery.view.GalleryFragment
import com.mgkim.sample.ui.home.view.HomeFragment
import com.mgkim.sample.ui.home.view.MainActivity
import com.mgkim.sample.ui.imagesearch.view.SearchImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindModule {
    @ContributesAndroidInjector
    abstract fun bindBaseActivity(): BaseActivity

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun bindMainActivity(): MainActivity

    // Home
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun bindHomeFragment(): HomeFragment

    // Gallery
    @ContributesAndroidInjector(modules = [GalleryModule::class])
    abstract fun bindGalleryFragment(): GalleryFragment

//    // Slideshow
//    @ContributesAndroidInjector(modules = [SlideshowModule::class])
//    abstract fun bindSlideshowFragment(): SlideshowFragment

    // Tools
    @ContributesAndroidInjector(modules = [SearchImageModule::class])
    abstract fun bindSearchImageFragment(): SearchImageFragment

}