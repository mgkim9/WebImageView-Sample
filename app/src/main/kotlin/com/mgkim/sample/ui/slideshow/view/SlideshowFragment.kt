package com.mgkim.sample.ui.slideshow.view

import androidx.lifecycle.Observer
import com.mgkim.sample.R
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.slideshow.model.SlideshowViewModel
import kotlinx.android.synthetic.main.fragment_slideshow.*
import javax.inject.Inject
@Deprecated("NotUse")
class SlideshowFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: SlideshowViewModel
    override fun getViewModel(): BaseViewModel? = viewModel
    override fun getLayoutId(): Int = R.layout.fragment_slideshow
    override fun initView() {

    }

    override fun dataBinding() {
        viewModel.text.observe(this, Observer {
            text_slideshow.text = it
        })
    }
}