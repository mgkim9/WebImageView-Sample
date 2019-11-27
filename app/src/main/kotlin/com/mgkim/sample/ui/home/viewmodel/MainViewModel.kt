package com.mgkim.sample.ui.home.viewmodel

import com.mgkim.sample.ui.base.BaseViewModel

class MainViewModel() : BaseViewModel() {
    fun loading(isLoading:Boolean) {
        mutableLoadingSubject.value = isLoading
    }
}