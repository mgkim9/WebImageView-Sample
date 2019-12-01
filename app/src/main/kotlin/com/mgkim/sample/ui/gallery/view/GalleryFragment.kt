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
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jakewharton.rxbinding2.view.RxView
import com.mgkim.libs.webimageview.RequestImageOn
import com.mgkim.libs.webimageview.widget.WebImageView
import com.mgkim.sample.R
import com.mgkim.sample.di.module.GlideApp
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveObserver
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.base.view.BaseRecyclerListAdapter
import com.mgkim.sample.ui.base.view.BaseViewHolder
import com.mgkim.sample.ui.gallery.viewmodel.GalleryViewModel
import com.mgkim.sample.utils.Log
import kotlinx.android.synthetic.main.fragment_gallery.*
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
                    val list: ArrayList<String> = ArrayList()
                    data.forEach {
                        //Webimage vs Glide 를 테스트하기위해 2개씩 Add
                        list.add(it)
                        list.add(it)
                    }
                    galleryAdapter.addItems(list)
                    rv_list.adapter?.notifyItemRangeInserted(0, list.size)
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
                viewModel.reqImages()   // Request 시작
            }
        }
    }

    override fun onListInteraction(item: String) {
        Toast.makeText(activity, "item : $item", Toast.LENGTH_LONG).show()
    }

    class GalleryAdapter: BaseRecyclerListAdapter<Void, String, Void>() {
        override fun getItemViewType(position: Int): Int {
            var type = super.getItemViewType(position)
            return if(type < 0) {
                type
            } else {
                //TODO item type
                (position + 4) % 4
            }
        }
        override fun getItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return createHolder(parent, viewType)
        }

        private fun createHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return when (viewType) {
                0 -> RequestImageCircleHolder(parent, R.layout.item_gallery_request_image)
                1 -> GlideViewCircleHolder(parent, R.layout.item_gallery_image)
                2 -> WebImageViewRoundedCornersHolder(parent, R.layout.item_gallery_webimageview)
                else -> GlideViewRoundedCornersHolder(parent, R.layout.item_gallery_image)
            }
        }

        inner class RequestImageCircleHolder(parent: ViewGroup, viewType: Int) :
            BaseViewHolder<String>(parent, viewType) {
            private val ivImage: ImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
//                RequestImageOn(data, ivImage.width, ivImage.height, NetManagerConfig.WebImageViewConfig(
//                    diskCacheOption = NetManagerConfig.DiskCacheOption.RESIZE_CACEH,
//                    isMemoryCache = true,
//                    preferredConfig = Bitmap.Config.ARGB_8888,
//                    defaultImageResId = com.mgkim.libs.webimageview.R.drawable.ic_default_picture,
//                    failImageResId = com.mgkim.libs.webimageview.R.drawable.ic_frown,
//                    animResId = android.R.anim.fade_in,
//                    progressResId =  -1,
//                    roundedCornerPixel = 30F,
//                    roundedCornerNoSquare = NetManagerConfig.RoundedCornerSquare.TOP_LEFT or NetManagerConfig.RoundedCornerSquare.BOTTOM_RIGHT,
//                    isResize = true,
//                    isBigSize = false
//                )).into(ivImage)
                RequestImageOn(data).makeRounded(0F).into(ivImage)
            }
        }

        inner class WebImageViewRoundedCornersHolder(parent: ViewGroup, viewType: Int) :
            BaseViewHolder<String>(parent, viewType) {
            private val ivImage: WebImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
                ivImage.setUrl(data)
            }
        }

        inner class GlideViewCircleHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder<String>(parent, viewType) {
            private val ivImage: ImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
                GlideApp.with(ivImage.context).load(data).transform(CircleCrop()).into(ivImage)
            }
        }

        inner class GlideViewRoundedCornersHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder<String>(parent, viewType) {
            private val ivImage: ImageView = view.findViewById(R.id.iv_image)
            override fun bind(data: String, position: Int) {
                GlideApp.with(ivImage.context).load(data).transform(RoundedCorners(30)).into(ivImage)
            }
        }
    }
}
