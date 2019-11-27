package com.mgkim.sample.network.api.kakao

import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import com.mgkim.sample.constants.KakaoApis.SEARCH_API
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface KSearchApi {
    //https://developers.kakao.com/docs/restapi/search#이미지-검색
    @GET("$SEARCH_API/image")
    fun getImages(
        @Query("query") query: String,  //검색을 원하는 질의어
        @Query("sort") sort: String? = null,    //결과 문서 정렬 방식
        @Query("page") page: Int? = null,   //결과 페이지 번호
        @Query("size") size: Int? = 50    //한 페이지에 보여질 문서의 개수
    ): Single<KakaoResult<List<KImageDto>>>
}