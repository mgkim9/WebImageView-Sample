package com.mgkim.sample.ui.map.model

import com.mgkim.sample.network.api.kakao.KMapApi
import com.mgkim.sample.network.dto.kakao.KPoiDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import io.reactivex.Single

class MapModel(private val mapApi: KMapApi) {
    fun getPois(
        code: String,
        rect: String?,
        x: String?,
        y: String?,
        radius: Int?,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null
    ): Single<KakaoResult<List<KPoiDto>>> =
        mapApi.getPois(code, rect, x, y, radius, sort, page, size)
}