package com.mgkim.sample.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mgkim.sample.utils.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.HttpsURLConnection

/**
 * BaseViewModel
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 6:55
 **/
open class BaseViewModel : ViewModel() {
    val TAG = javaClass.simpleName
    private val jobDisposable = CompositeDisposable()

    /**
     * loading을 제어할 LiveData
     */
    protected val mutableLoadingSubject = MutableLiveData<Boolean>()
    val loadingSubject: LiveData<Boolean> get() = mutableLoadingSubject

//    protected val mutableEmptySubject = MutableLiveData<LiveWrapper<Void>>()
//    val emptySubject: LiveData<LiveWrapper<Void>> get() = mutableEmptySubject
//    val emptyWrapper = LiveWrapper<Void>()

    fun addDisposable(disposable: Disposable) {
        jobDisposable.add(disposable)
    }

    /**
     * Single Request
     */
    protected fun <T : Any?> request(single: Single<T>, singleObserver: DisposableSingleObserver<T>) {
        single.onErrorResumeNext(errorHandler<T>(single) as Function<Throwable, Single<T>>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(singleObserver)
        jobDisposable.add(singleObserver)
    }

    /**
     * Observer Request
     */
    protected fun <T : Any?> request(observable: Observable<T>, disposableObserver: DisposableObserver<T>) {
        observable.onErrorResumeNext(errorHandler<T>(observable) as Function<Throwable, Observable<T>>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver)
        jobDisposable.add(disposableObserver)
    }

    /**
     * error handling
     */
    private fun <T> errorHandler(toBeResumed: Any): Function<Throwable, Any> {
        return Function { throwable: Throwable ->
            var isError  = true
            if (throwable is HttpException) {
                val errorCode = throwable.code()
                isError = when (errorCode) {
                    HttpsURLConnection.HTTP_NOT_FOUND -> {
                        Log.e(TAG, "Http404Error throwable $throwable")
                        true
                    }
                    HttpsURLConnection.HTTP_CLIENT_TIMEOUT -> {
                        Log.e(TAG, "Http408Error throwable $throwable")
                        true
                    }
                    else -> {
                        Log.e(TAG, "Http${errorCode}Error throwable $throwable")
                        true
                    }
                }
            }

            //retry 해야 할 상황에서만 isError true로셋팅.
            if (isError) {
                when (toBeResumed) {
                    is Single<*> -> return@Function Single.error<T>(throwable) as Single<T>
                    is Observable<*> -> return@Function Observable.error<T>(throwable) as Observable<T>
                    else -> Log.e(TAG, "Error throwable $throwable")
                }
            }

            return@Function toBeResumed
        }
    }

    /**
     * api 재실행
     * @param observable : 실행할 api observable
     * @param retryCnt : 재실행 횟수
     * @return
     */
    fun <T> retryObservable(observable: Observable<T>, retryCnt: Int): Observable<T> {
        return retryObservable(observable, retryCnt, 0)
    }

    private fun <T> retryObservable(observable: Observable<T>, retryCnt: Int, delay: Long): Observable<T> {
        return observable.retryWhen(Function<Observable<out Throwable>, Observable<*>> { observable ->
            val counter = AtomicInteger()
            observable.takeWhile { counter.getAndIncrement() < retryCnt + 1 }.flatMap(Function<Throwable, ObservableSource<*>> { throwable ->
                if (counter.get() > retryCnt) {
                    Log.e(TAG, "retry fail throwable $throwable")
                    return@Function Observable.error<Any>(throwable)
                }

                if (throwable is HttpException){
                    val errorCode = throwable.code()
                    when (errorCode) {
                        HttpsURLConnection.HTTP_NOT_FOUND -> {
                            Log.e(TAG, "no retry Http404Error throwable $throwable")
                            Observable.error<Any>(throwable)
                        }
                        HttpsURLConnection.HTTP_CLIENT_TIMEOUT -> {
                            Log.e(TAG, "no retry Http408Error throwable $throwable")
                            Observable.error<Any>(throwable)
                        }
                        else -> {
                            Log.e(TAG, "retry Http${errorCode}Error throwable $throwable")
                            Observable.timer(delay, TimeUnit.SECONDS)
                        }
                    }
                } else {
                    Log.e(TAG, "retry " + counter.get() + " throwable " + throwable)
                    Observable.timer(delay, TimeUnit.SECONDS)
                }
            })
        })
    }

    override fun onCleared() {
        super.onCleared()
        jobDisposable.dispose()
    }
}