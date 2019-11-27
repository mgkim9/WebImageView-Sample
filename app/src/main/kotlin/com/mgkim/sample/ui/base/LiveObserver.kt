package com.mgkim.sample.ui.base

import androidx.lifecycle.Observer

/**
* Request 상태를 전달할 Observer
* @author : mgkim
* @version : 1.0.0
* @since : 2019-11-27 오후 7:05
**/
abstract class LiveObserver<T> : Observer<LiveWrapper<T>> {

    override fun onChanged(wrapper: LiveWrapper<T>?) {
        wrapper?.run {
            when (status) {
                LiveWrapper.Status.LOADING -> onLoading(loading!!)
                LiveWrapper.Status.SUCCESS -> onSuccess(data)
                LiveWrapper.Status.ERROR -> onError(error!!, retryCallback, cancelCallback)
            }
            return
        }
        onError(Exception("Something went wrong"))
    }

    abstract fun onSuccess(data: T?)

    abstract fun onError(error: Throwable, retryCallback: (() -> Unit?)? = null, cancelCallback: (() -> Unit?)? = null)

    abstract fun onLoading(loading: Boolean)
}