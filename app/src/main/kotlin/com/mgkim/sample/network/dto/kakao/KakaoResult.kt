package com.mgkim.sample.network.dto.kakao

data class KakaoResult<D>(
    val meta: KMeta,
    val documents: D
)