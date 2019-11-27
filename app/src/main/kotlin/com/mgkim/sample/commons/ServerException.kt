package com.mgkim.sample.commons

import java.io.IOException

class ServerException(private var code: String, message: String?, private var body: String? = null) : IOException(message) {

    private var text: String? = null
    private var subText: String? = null

    fun code(): String {
        return code
    }

    fun setText(text: String) {
        this.text = text
    }

    fun text(): String? {
        return text
    }

    fun setSubText(subText: String) {
        this.subText = subText
    }

    fun subText(): String? {
        return subText
    }

    fun body(): String? {
        return body
    }
}