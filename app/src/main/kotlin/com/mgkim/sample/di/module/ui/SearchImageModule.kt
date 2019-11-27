package com.mgkim.sample.di.module.ui

import androidx.lifecycle.ViewModelProviders
import com.mgkim.sample.network.api.kakao.KSearchApi
import com.mgkim.sample.ui.base.ViewModelProviderFactory
import com.mgkim.sample.ui.imagesearch.model.SearchImageModel
import com.mgkim.sample.ui.imagesearch.view.SearchImageFragment
import com.mgkim.sample.ui.imagesearch.viewmodel.SearchImageViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchImageModule {

    @Provides
    fun provideSearchImageModel(fragment: SearchImageFragment, model: SearchImageModel): SearchImageViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(SearchImageViewModel(model)))[SearchImageViewModel::class.java]

    @Provides
    fun provideToolsModel(searchApi: KSearchApi): SearchImageModel = SearchImageModel(searchApi)
}