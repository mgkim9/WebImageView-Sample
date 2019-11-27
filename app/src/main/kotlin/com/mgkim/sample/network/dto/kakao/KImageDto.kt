package com.mgkim.sample.network.dto.kakao

data class KImageDto(
    val collection: String, //컬렉션
    val thumbnail_url: String, //이미지 썸네일 URL
    val image_url: String, //이미지 URL
    val width: Integer,//이미지의 가로 크기
    val height: Integer, //이미지의 세로 크기
    val display_sitename: String,//출처명
    val doc_url: String, //문서 URL
    val datetime: String //문서 작성시간. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
)