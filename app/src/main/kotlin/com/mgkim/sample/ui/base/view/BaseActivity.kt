package com.mgkim.sample.ui.base.view

import android.os.Bundle
import androidx.lifecycle.Observer
import com.mgkim.sample.repository.preferences.AppPreference
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.dialog.LoadingDialog
import com.mgkim.sample.ui.base.dialog.MessageDialog
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * BaseActivity
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 6:53
 **/
abstract class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var appPreference: AppPreference

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getContentView())
        loadingDialog = LoadingDialog(this)

        getViewModel()?.apply {
            loadingSubject.observe(this@BaseActivity, Observer {
                if (it == true) showLoading() else dismissLoading()
            })
        }
        initView()
        dataBinding()
    }

    fun showLoading() {
        if (isFinishing) return
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (isFinishing) return
        loadingDialog.dismiss()
    }

    fun showMessageDialog(content: String?, cancelCallback: (() -> Unit?)? = null) {
        showMessageDialog(content, null, cancelCallback)
    }

    fun showMessageDialog(content: String?, subContent: String? = null, cancelCallback: (() -> Unit?)? = null) {
        MessageDialog(this).apply {
            if (!subContent.isNullOrEmpty()) {
                setTitleAndContent(content ?: "", subContent)
            } else {
                setContent(content ?: "")
            }
            setPositiveButton(null)
            if (cancelCallback != null) {
                setOnDismissListener {
                    cancelCallback()
                }
            }
        }.show()
    }

    /**
     * viewModel
     */
    abstract fun getViewModel(): BaseViewModel?
    /**
     * layout id
     */
    abstract fun getContentView(): Int
    /**
     * view 초기화
     */
    abstract fun initView()

    /**
     * data binding
     */
    abstract fun dataBinding()
}