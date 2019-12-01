package com.mgkim.sample.ui.home.view

import androidx.lifecycle.Observer
import com.mgkim.libs.webimageview.NetManager
import com.mgkim.libs.webimageview.RequestImage
import com.mgkim.sample.R
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.home.viewmodel.HomeViewModel
import com.mgkim.sample.utils.Log
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: HomeViewModel
    override fun getViewModel(): BaseViewModel? = viewModel
    override fun getLayoutId(): Int = R.layout.fragment_home
    override fun initView() {
        llParent.setOnClickListener {
            Log.i(TAG, "llParent")
        }

        llMid.setOnClickListener {
            Log.i(TAG, "llMid")
        }

        llBottom.setOnClickListener {
            Log.i(TAG, "llBottom")
        }
    }

    override fun dataBinding() {
        viewModel.text.observe(this, Observer {
            text_home.text = it
        })
    }
}