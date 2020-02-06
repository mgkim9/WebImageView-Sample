package com.mgkim.sample.network.api.kakao

import com.mgkim.sample.constants.KakaoApis
import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import com.mgkim.sample.constants.KakaoApis.SEARCH_API
import com.mgkim.sample.network.dto.kakao.KPoiDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface KMapApi {
    //https://developers.kakao.com/docs/restapi/local#카테고리로-장소-검색
    @GET("${KakaoApis.MAP_API}/search/category.json")
    fun getPois(
        @Query("category_group_code") code: String,  //카테고리 그룹 코드
        @Query("rect") rectv: String? = null,    //rect
        @Query("x") x: String? = null,    //longitude
        @Query("y") y: String? = null,    //latitude
        @Query("radius") radius: Int? = null,    //중심 좌표부터의 반경거리 단위 : meter (0~20000 Integer)
        @Query("sort") sort: String? = null,    //결과 문서 정렬 방식
        @Query("page") page: Int? = null,   //결과 페이지 번호
        @Query("size") size: Int? = 50    //한 페이지에 보여질 문서의 개수
    ): Single<KakaoResult<List<KPoiDto>>>
}