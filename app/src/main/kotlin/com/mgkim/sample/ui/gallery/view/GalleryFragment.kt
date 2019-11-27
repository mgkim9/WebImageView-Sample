package com.mgkim.sample.ui.gallery.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.mgkim.sample.R
import com.mgkim.sample.di.module.GlideApp
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveObserver
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.base.view.BaseRecyclerListAdapter
import com.mgkim.sample.ui.base.view.BaseViewHolder
import com.mgkim.sample.ui.gallery.viewmodel.GalleryViewModel
import com.mgkim.sample.utils.Log
import com.mgkim.libs.webimageview.widget.WebImageView
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.rv_list
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class GalleryFragment : BaseFragment() , View.OnClickListener, BaseRecyclerListAdapter.OnListInteractionListener<String> {
    @Inject
    lateinit var viewModel: GalleryViewModel
    override fun getViewModel(): BaseViewModel? = viewModel

    override fun getLayoutId(): Int = R.layout.fragment_gallery

    private val columnCount = 4


    private val title : MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = "Image Parse Test"
        }
    }

    lateinit var galleryAdapter: GalleryAdapter
    private var clickCnt = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun initView() {
        retainInstance = true
        galleryAdapter = GalleryAdapter().apply {
            mListener = this@GalleryFragment
        }
        with(rv_list) {
            adapter = galleryAdapter
        }
        RxView.clicks(btnParse).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
            onClick(btnParse)
        }
    }

    override fun dataBinding() {
        title.observe(this, Observer {
            text_gallery.text = it
        })

        viewModel.images.observe(this, object : LiveObserver<List<String>>() {
            override fun onSuccess(data: List<String>?) {
                if (data == null) {

                } else {
                    galleryAdapter.addItems(data)
                    rv_list.adapter?.notifyItemRangeInserted(0, data.size)
                }
            }

            override fun onError(error: Throwable, retryCallback: (() -> Unit?)?, cancelCallback: (() -> Unit?)?) {
                Log.d(TAG, "ERROR : $error")
            }

            override fun onLoading(loading: Boolean) {}
        })

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val columnCount = if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) columnCount else columnCount * 2
        with(rv_list) {
            (layoutManager as? GridLayoutManager)?.apply {
                spanCount = columnCount
            }
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnParse -> {
                galleryAdapter.clearItems()
                galleryAdapter.type = ++clickCnt%2
                title.value = if (galleryAdapter.type == 0) "Glide로 호출" else "WebImageView로 호출"
                viewModel.reqImages()   // Request 시작
            }
        }
    }

    override fun onListInteraction(item: String) {
        Toast.makeText(activity, "item : $item", Toast.LENGTH_LONG).show()
    }

    class GalleryAdapter: BaseRecyclerListAdapter<Void, String, Void>() {
        var type = 0

        override fun getItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return if(type == 0) GlideViewHolder(parent, R.layout.item_gallery_image_glide) else WebImageViewHolder(parent, R.layout.item_gallery_image)
        }

        inner class GlideViewHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder<String>(parent, viewType) {
            private val ivImage: ImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
                GlideApp.with(ivImage.context).load(data).into(ivImage)
            }
        }

        inner class WebImageViewHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder<String>(parent, viewType) {
            private val ivImage: WebImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
                ivImage.setUrl(data)
            }
        }
    }
}
