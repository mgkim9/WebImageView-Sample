package com.mgkim.sample.ui.slideshow.view

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding2.view.RxView
import com.mgkim.sample.R
import com.mgkim.sample.network.dto.test.PhotoDto
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveObserver
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.slideshow.model.SlideshowViewModel
import com.mgkim.sample.utils.Log
import kotlinx.android.synthetic.main.fragment_slideshow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SlideshowFragment : BaseFragment(), View.OnClickListener {
    @Inject
    lateinit var viewModel: SlideshowViewModel
    override fun getViewModel(): BaseViewModel? = viewModel
    override fun getLayoutId(): Int = R.layout.fragment_slideshow
    override fun initView() {
        RxView.clicks(btnRequestPhoto).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
            onClick(btnRequestPhoto)
        }

        RxView.clicks(btnRequestPhotos).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
            onClick(btnRequestPhotos)
        }
    }

    override fun dataBinding() {
        viewModel.text.observe(this, Observer {
            text_slideshow.text = it
        })

        viewModel.image.observe(this, object : LiveObserver<PhotoDto>() {
            override fun onSuccess(data: PhotoDto?) {
                if (data == null) {

                } else {
                    tvOutput.text = data.toString()
                    Toast.makeText(activity, "RequestAPI Success", Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(error: Throwable, retryCallback: (() -> Unit?)?, cancelCallback: (() -> Unit?)?) {
                Log.d(TAG, "ERROR : $error")
            }

            override fun onLoading(loading: Boolean) {}
        })

        viewModel.images.observe(this, object : LiveObserver<Array<PhotoDto>>() {
            override fun onSuccess(data: Array<PhotoDto>?) {
                if (data == null) {

                } else {
                    tvOutput.text = " PhotoDto size : ${data.size}"
                    Toast.makeText(activity, "RequestAPI Success", Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(error: Throwable, retryCallback: (() -> Unit?)?, cancelCallback: (() -> Unit?)?) {
                Log.d(TAG, "ERROR : $error")
            }

            override fun onLoading(loading: Boolean) {}
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnRequestPhoto -> viewModel.requestPhoto(1)
            R.id.btnRequestPhotos -> viewModel.requestPhotos()
        }
    }
}
