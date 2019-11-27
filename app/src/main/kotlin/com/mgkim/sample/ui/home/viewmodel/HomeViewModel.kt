package com.mgkim.sample.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.sample.ui.home.model.HomeModel
import com.mgkim.sample.ui.base.BaseViewModel

class HomeViewModel(homeModel: HomeModel) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}