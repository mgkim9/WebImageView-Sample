package com.mgkim.sample.network.dto.kakao

data class KMeta(
    val total_count: Int,//검색어에 검색된 문서수
    val pageable_count: Int,//total_count 중에 노출가능 문서수
    val is_end: Boolean//현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음.
)