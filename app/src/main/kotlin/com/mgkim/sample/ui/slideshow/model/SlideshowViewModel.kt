package com.mgkim.sample.ui.slideshow.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.libs.webimageview.RequestAPI
import com.mgkim.sample.constants.JSONPLACEHOLDER_PHOTOS_URL
import com.mgkim.sample.network.dto.test.PhotoDto
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveWrapper

class SlideshowViewModel : BaseViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "API Request Test"
    }
    val text: LiveData<String> = _text

    private var mutableImage = MutableLiveData<LiveWrapper<PhotoDto>>()
    val image: LiveData<LiveWrapper<PhotoDto>> get() = mutableImage

    private var mutableImages = MutableLiveData<LiveWrapper<Array<PhotoDto>>>()
    val images: LiveData<LiveWrapper<Array<PhotoDto>>> get() = mutableImages

    //Reqest Photo
    fun requestPhoto(id: Int) {
        val wrapper = LiveWrapper<PhotoDto>()
        mutableLoadingSubject.value = true
        RequestAPI(PhotoDto::class.java, "$JSONPLACEHOLDER_PHOTOS_URL/$id")
            .setReceiver { isSuccess, obj ->
                mutableLoadingSubject.value = false
                if (isSuccess) {
                    mutableImage.value = wrapper.success(obj.getResult())
                } else {
                    mutableImage.value = wrapper.error(throw Throwable(), fun() {
                        requestPhoto(id)
                    })
                }
            }.useHandler().addReq()
    }

    //Reqest Photos
    fun requestPhotos() {
        val wrapper = LiveWrapper<Array<PhotoDto>>()
        mutableLoadingSubject.value = true
        RequestAPI(Array<PhotoDto>::class.java, JSONPLACEHOLDER_PHOTOS_URL)
            .setReceiver { isSuccess, obj ->
                mutableLoadingSubject.value = false
                if (isSuccess) {
                    mutableImages.value = wrapper.success(obj.getResult())
                } else {
                    mutableImages.value = wrapper.error(throw Throwable(), fun() {
                        requestPhotos()
                    })
                }
            }.useHandler().addReq()
    }
}