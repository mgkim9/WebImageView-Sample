package com.mgkim.sample.ui.slideshow.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.sample.ui.base.BaseViewModel

@Deprecated("NotUse")
class SlideshowViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text
}