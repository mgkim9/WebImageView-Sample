package com.mgkim.sample.constants

import com.mgkim.sample.BuildConfig

internal var IS_DEBUG: Boolean = BuildConfig.DEBUG

// network
internal const val KAKAO_REST_API_KEY = "478710b0eed404a996161a06133ef260"
internal const val BASE_URL = "https://dapi.kakao.com/"
internal const val GETTY_URL = "https://www.gettyimagesgallery.com/collection/sasha/"


internal const val FILE_DIRECTORY = "/Sample"

object KakaoApis {
    const val SEARCH_API = "/v2/search/"
}