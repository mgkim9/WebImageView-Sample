package com.mgkim.sample.ui.imagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveWrapper
import com.mgkim.sample.ui.imagesearch.model.SearchImageModel
import io.reactivex.observers.DisposableSingleObserver

class SearchImageViewModel(private val searchImageModel: SearchImageModel) : BaseViewModel() {
    private var mutableImages = MutableLiveData<LiveWrapper<KakaoResult<List<KImageDto>>>>()
    val images: LiveData<LiveWrapper<KakaoResult<List<KImageDto>>>> get() = mutableImages

    fun reqImages(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
        cancelCallback: (() -> Unit?)? = null
    ) {
        val wrapper = LiveWrapper<KakaoResult<List<KImageDto>>>()
        request(searchImageModel.getImages(query, sort, page, size).doOnSubscribe {
            if (page ?: 1 < 2) {
                mutableLoadingSubject.postValue(true)
            }
        }.doAfterTerminate {
            mutableLoadingSubject.postValue(false)
        }, object : DisposableSingleObserver<KakaoResult<List<KImageDto>>>() {
            override fun onSuccess(t: KakaoResult<List<KImageDto>>) {
                mutableImages.value = wrapper.success(t)
            }

            override fun onError(e: Throwable) {
                //공통 Error 처리 Retry
                mutableImages.value = wrapper.error(e, fun() {
                    reqImages(query, sort, page, size, cancelCallback)
                }, cancelCallback)
            }
        })
    }
}