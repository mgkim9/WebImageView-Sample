package com.mgkim.sample.constants

import com.mgkim.sample.BuildConfig

internal var IS_DEBUG: Boolean = BuildConfig.DEBUG

// network
internal const val KAKAO_REST_API_KEY = "407392519301f1218d29d388721bcc0e"
internal const val BASE_URL = "https://dapi.kakao.com/"
internal const val GETTY_URL = "https://www.gettyimagesgallery.com/collection/sasha/"
internal const val JSONPLACEHOLDER_PHOTOS_URL = "https://jsonplaceholder.typicode.com/photos"


internal const val FILE_DIRECTORY = "/Sample"

object KakaoApis {
    const val SEARCH_API = "/v2/search"
    const val MAP_API = "/v2/local"
}