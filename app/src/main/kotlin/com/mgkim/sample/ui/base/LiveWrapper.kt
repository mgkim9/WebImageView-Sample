package com.mgkim.sample.ui.base
/**
* Request 상태를 갖도록 감싸주는 Wrapper class
* @author : mgkim
* @version : 1.0.0
* @since : 2019-11-27 오후 7:04
**/
class LiveWrapper<T> {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    var status: Status? = null
    var data: T? = null
    var error: Throwable? = null
    var loading: Boolean? = null
    var retryCallback: (() -> Unit?)? = null
    var cancelCallback: (() -> Unit?)? = null

    fun success(data: T?): LiveWrapper<T> {
        this.status = Status.SUCCESS
        this.data = data
        return this
    }

    fun error(error: Throwable, retryCallback: (() -> Unit?)? = null, cancelCallback: (() -> Unit?)? = null): LiveWrapper<T> {
        this.status = Status.ERROR
        this.error = error
        this.retryCallback = retryCallback
        this.cancelCallback = cancelCallback
        return this
    }

    fun loading(loading: Boolean): LiveWrapper<T> {
        this.status = Status.LOADING
        this.loading = loading
        return this
    }
}