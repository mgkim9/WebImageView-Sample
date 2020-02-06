package com.mgkim.sample.ui.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.sample.network.dto.kakao.KPoiDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveWrapper
import com.mgkim.sample.ui.map.model.MapModel
import io.reactivex.observers.DisposableSingleObserver

class MapViewModel(private val mapModel: MapModel) : BaseViewModel() {
    private var mutablePois = MutableLiveData<LiveWrapper<KakaoResult<List<KPoiDto>>>>()
    val pois: LiveData<LiveWrapper<KakaoResult<List<KPoiDto>>>> get() = mutablePois

    fun reqPois(
        code: String,
        rect: String?,
        x: String?,
        y: String?,
        radius: Int?,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
        cancelCallback: (() -> Unit?)? = null
    ) {
        val wrapper = LiveWrapper<KakaoResult<List<KPoiDto>>>()
        request(mapModel.getPois(code, rect, x, y, radius, sort, page, size).doOnSubscribe {
            if (page ?: 1 < 2) {
                mutableLoadingSubject.postValue(true)
            }
        }.doAfterTerminate {
            mutableLoadingSubject.postValue(false)
        }, object : DisposableSingleObserver<KakaoResult<List<KPoiDto>>>() {
            override fun onSuccess(t: KakaoResult<List<KPoiDto>>) {
                mutablePois.value = wrapper.success(t)
            }

            override fun onError(e: Throwable) {
                //공통 Error 처리 Retry
                mutablePois.value = wrapper.error(e, fun() {
                    reqPois(code, rect, x, y, radius, sort, page, size, cancelCallback)
                }, cancelCallback)
            }
        })
    }
}