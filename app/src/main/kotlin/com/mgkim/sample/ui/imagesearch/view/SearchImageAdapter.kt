package com.mgkim.sample.ui.imagesearch.view


import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mgkim.libs.webimageview.RequestImageOn
import com.mgkim.libs.webimageview.widget.WebImageView
import com.mgkim.sample.R
import com.mgkim.sample.di.module.GlideApp
import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.ui.base.view.BaseRecyclerListAdapter
import com.mgkim.sample.ui.base.view.BaseViewHolder

class SearchImageAdapter(mListener: OnListInteractionListener<KImageDto>?) : BaseRecyclerListAdapter<Void, KImageDto,Void >() {
    init {
        this.mListener = mListener
    }
    override fun getItemViewType(position: Int): Int {
        var type = super.getItemViewType(position)
        return if(type < 0) {
            type
        } else {
            //TODO item type
            (position + 4) % 4
        }
    }

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<KImageDto> {
        return createHolder(parent, viewType)
    }

    private fun createHolder(parent: ViewGroup, viewType: Int): ViewHolder<KImageDto> {
        return when (viewType) {
            0 -> RequestImageCircleHolder(parent, R.layout.item_search_request_image)
            1 -> GlideViewCircleHolder(parent, R.layout.item_search_image)
            2 -> WebImageViewRoundedCornersHolder(parent, R.layout.item_search_webimageview)
            else -> GlideViewRoundedCornersHolder(parent, R.layout.item_search_image)
        }
    }

    open abstract class ViewHolder<E>(parent: ViewGroup, viewType: Int) : BaseViewHolder<E>(parent, viewType) {
        val mIdView: TextView = view.findViewById(R.id.item_number)
        val mContentView: TextView = view.findViewById(R.id.content)
        override fun bind(data: E, position: Int) {
        }
    }

    inner class RequestImageCircleHolder(parent: ViewGroup, viewType: Int) :
        ViewHolder<KImageDto>(parent, viewType) {
        private val ivImage: ImageView = view.findViewById(R.id.iv_image)
        override fun bind(data: KImageDto, position: Int) {
            mIdView.text = data.collection
            mContentView.text = data.display_sitename
            RequestImageOn(data.thumbnail_url).makeRounded(0F).into(ivImage)
        }
    }

    inner class WebImageViewRoundedCornersHolder(parent: ViewGroup, viewType: Int) :
        ViewHolder<KImageDto>(parent, viewType) {
        private val ivImage: WebImageView = view.findViewById(R.id.iv_image)
        override fun bind(data: KImageDto, position: Int) {
            mIdView.text = data.collection
            mContentView.text = data.display_sitename
            ivImage.setUrl(data.thumbnail_url)
        }
    }

    inner class GlideViewCircleHolder(parent: ViewGroup, viewType: Int) : ViewHolder<KImageDto>(parent, viewType) {
        private val ivImage: ImageView = view.findViewById(R.id.iv_image)
        override fun bind(data: KImageDto, position: Int) {
            mIdView.text = data.collection
            mContentView.text = data.display_sitename
            GlideApp.with(ivImage.context).load(data.thumbnail_url).transform(CircleCrop()).into(ivImage)
        }
    }

    inner class GlideViewRoundedCornersHolder(parent: ViewGroup, viewType: Int) : ViewHolder<KImageDto>(parent, viewType) {
        private val ivImage: ImageView = view.findViewById(R.id.iv_image)
        override fun bind(data: KImageDto, position: Int) {
            mIdView.text = data.collection
            mContentView.text = data.display_sitename
            GlideApp.with(ivImage.context).load(data.thumbnail_url).transform(RoundedCorners(30)).into(ivImage)
        }
    }

}
