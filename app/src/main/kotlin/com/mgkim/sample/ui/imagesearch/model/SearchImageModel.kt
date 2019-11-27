package com.mgkim.sample.ui.imagesearch.model

import com.mgkim.sample.network.api.kakao.KSearchApi
import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import io.reactivex.Single

class SearchImageModel(private val searchApi: KSearchApi) {
    fun getImages(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null
    ): Single<KakaoResult<List<KImageDto>>> = searchApi.getImages(query, sort, page, size)
}