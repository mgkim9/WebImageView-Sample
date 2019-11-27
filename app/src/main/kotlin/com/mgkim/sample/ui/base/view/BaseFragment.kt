package com.mgkim.sample.ui.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.mgkim.sample.ui.base.BaseViewModel
import dagger.android.support.DaggerFragment

/**
 * BaseFragment
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 6:54
 **/
abstract class BaseFragment : DaggerFragment() {
    val TAG = javaClass.simpleName

    private lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        getViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getViewModel()?.apply {
            loadingSubject.observe(baseActivity, Observer {
                if (it == true) showLoading() else dismissLoading()
            })
        }
        initView()
        dataBinding()
    }

    protected fun getBaseActivity(): BaseActivity = baseActivity

    protected fun showLoading() = getBaseActivity().showLoading()

    protected fun dismissLoading() = getBaseActivity().dismissLoading()

    protected fun showMessageDialog(content: String?) = getBaseActivity().showMessageDialog(content)

    protected fun finish() = activity?.finish()

    /**
     * viewModel
     */
    abstract fun getViewModel(): BaseViewModel?
    /**
     * layout id
     */
    abstract fun getLayoutId(): Int
    /**
     * view 초기화
     */
    abstract fun initView()

    /**
     * data binding
     */
    abstract fun dataBinding()
}