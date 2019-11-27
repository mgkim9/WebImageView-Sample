package com.mgkim.sample.ui.base.view

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mgkim.sample.R
import com.mgkim.sample.interfaces.ListMoreActionListener
import kotlinx.android.synthetic.main.adapter_more_footer.view.*

/**
 * BaseRecyclerListAdapter
 * RecyclerView를 편하게 사용 하기위한 base class
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 6:33
 * @param H : Header 객체 Type
 * @param E : List Item 객체 Type
 * @param F : Footer 객체 Type
 **/
abstract class BaseRecyclerListAdapter<H, E, F> : RecyclerView.Adapter<BaseViewHolder<*>>() {
    val TAG: String = javaClass.simpleName
    protected companion object {
        const val TYPE_HEADER = -1
        const val TYPE_FOOTER = -2
        const val TYPE_MORE = -3
    }
    var mListener: OnListInteractionListener<E>? = null
    /**
    * ListItem click event를 전달하기위한 interface
    * @author : mgkim
    * @version : 1.0.0
    * @since : 2019-11-27 오후 6:37
     * @param E : List Item 객체 Type
    **/
    interface OnListInteractionListener<E> {
        fun onListInteraction(item: E)
    }
    private val mOnClickListener: View.OnClickListener
    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as E
            mListener?.onListInteraction(item)
        }
    }

    private lateinit var callback: ListMoreActionListener

    private var header: H? = null
    private var footer: F? = null

    /**
     * item 목록
     */
    private var items: MutableList<E> = mutableListOf()

    /**
     * more 동작 구현 여부
     */
    private var isMore: Boolean = false

    /**
     * list 하단에 접근 시 자동 More 여부
     */
    private var isAutoMore: Boolean = false

    /**
     * 현재 More 중인지
     */
    private var isLoading: Boolean = false

    /**
     * 다음 페이지가 있는지
     */
    private var hasNextPage = true

    /**
     * AutoMore true일때 More가 동작하는 마지막 index 번호
     */
    private var visibleThreshold: Int = 0

    /**
     * 현재 page
     */
    private var page = 1

    /**
     * UI 작업을 위한 Handler
     */
    private val handler = Handler()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_HEADER -> getHeaderViewHolder(parent)!!
            TYPE_FOOTER -> getFooterViewHolder(parent)!!
            TYPE_MORE -> getMoreViewHolder(parent)
            else -> getItemViewHolder(parent, viewType)
        }
    }

    /**
     * Header 사용시 구현 필요
     */
    open fun getHeaderViewHolder(parent: ViewGroup): BaseViewHolder<H>? = null

    /**
     * Footer 사용시 구현 필요
     */
    open fun getFooterViewHolder(parent: ViewGroup): BaseViewHolder<F>? = null

    /**
     * viewType 별 ItemViewHolder 생성
     */
    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<E>

    /**
     * More UI 변경 필요 시 구현
     */
    open fun getMoreViewHolder(parent: ViewGroup): BaseViewHolder<Any> = MoreHolder(parent)

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (isMore && isAutoMore) onLoadMore(position)  // more 체크

        when {
            header != null && position == getHeaderPosition() -> onBindHeaderViewHolder(holder as BaseViewHolder<H>, position, payloads)
            footer != null && position == getFooterPosition() -> onBindFooterViewHolder(holder as BaseViewHolder<F>, position, payloads)
            isMore && position == getMorePosition() -> onBindMoreViewHolder(holder as BaseViewHolder<Any>, position, payloads)
            else -> {
                onBindItemViewHolder(holder as BaseViewHolder<E>, getItemPosition(position), payloads)
                if(holder.isClickable) {
                    with(holder.view) {
                        tag = getItem(position)
                        setOnClickListener(mOnClickListener)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {}

    protected open fun onBindHeaderViewHolder(holder: BaseViewHolder<H>, position: Int, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            holder.bind(header!!, position)
        } else {
            holder.changed(header!!, position, payloads)
        }
    }

    protected open fun onBindFooterViewHolder(holder: BaseViewHolder<F>, position: Int, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            holder.bind(footer!!, position)
        } else {
            holder.changed(footer!!, position, payloads)
        }
    }

    protected open fun onBindItemViewHolder(holder: BaseViewHolder<E>, position: Int, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            holder.bind(getItem(position) as E, position)
        } else {
            holder.changed(getItem(position) as E, position, payloads)
        }
    }

    protected open fun onBindMoreViewHolder(holder: BaseViewHolder<Any>, position: Int, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            holder.bind(isLoading, position)
        } else {
            holder.changed(isLoading, position, payloads)
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder<*>) {
        holder.unbind()
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            header != null && position == getHeaderPosition() -> TYPE_HEADER
            footer != null && position == getFooterPosition() -> TYPE_FOOTER
            isMore && position == getMorePosition() -> TYPE_MORE
            else -> super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        var itemCount = getItemSize()
        if (header != null) itemCount++
        if (footer != null) itemCount++
        if (isMore) itemCount++
        return itemCount
    }

    /**
     * item size
     */
    fun getItemSize(): Int = items.size

    fun clearItems() {
        hasNextPage = true
        page = 1
        hideLoading()
        items.clear()
        notifyDataSetChanged()
    }

    /**
     * item  1개 추가
     */
    fun addItem(item: E) {
        items.add(item)
        var position = getItemSize() - 1
        if (header != null) position++
        notifyItemInserted(position)
    }

    fun getItem(position: Int): E? = if (position < 0 || items.size <= position) null else items[position]

    /**
     * item 복수 추가 (notify 필요)
     */
    fun addItems(items: List<E>) {
        this.items.addAll(items.toMutableList())
    }

    /**
     * item list  변경 (notify 필요)
     */
    fun setItems(items: List<E>) {
        this.items = items.toMutableList()
    }

    /**
     * header 추가
     */
    fun setHeader(header: H) {
        this.header = header
    }

    /**
     * footer 추가
     */
    fun setFooter(footer: F) {
        this.footer = footer
    }

    fun getHeader(): H? = header

    fun getFooter(): F? = footer

    open fun getItemPosition(position: Int): Int {
        var itemPosition = position
        if (header != null) itemPosition--
        return itemPosition
    }

    private fun getHeaderPosition(): Int = 0

    private fun getFooterPosition(): Int = itemCount - 1

    private fun getMorePosition(): Int {
        var position = itemCount - 1
        if (footer != null) position--
        return position
    }

    fun notifyHeaderChanged() = notifyItemChanged(getHeaderPosition())

    fun notifyHeaderChanged(payload: Any) = notifyItemChanged(getHeaderPosition(), payload)

    fun notifyFooterChanged() = notifyItemChanged(getFooterPosition())

    fun notifyFooterChanged(payload: Any) = notifyItemChanged(getFooterPosition(), payload)

    fun notifyItemChanged() {
        var position = 0
        if (header != null) position++
        notifyItemRangeChanged(position, getItemSize())
    }

    fun notifyItemChanged(payload: Any) {
        var position = 0
        if (header != null) position++
        notifyItemRangeChanged(position, getItemSize(), payload)
    }

    /**
     * More 동작 적용
     */
    fun setMoreActionListener(callback: ListMoreActionListener, isAutoMore: Boolean = false, visibleThreshold:Int = 0) {
        isMore = true
        this.callback = callback
        this.isAutoMore = isAutoMore
        this.visibleThreshold = visibleThreshold
    }

    //----------------------------------------------------------------------------------------------
    // Auto Loading
    //----------------------------------------------------------------------------------------------
    private fun onLoadMore(position: Int) {
        var lastVisiblePosition = position + 1
        var count = itemCount

        lastVisiblePosition--
        count--

        if (header != null) {
            lastVisiblePosition--
            count--
        }

        if (footer != null) {
            lastVisiblePosition--
            count--
        }

        if (!isLoading && count > 0 && lastVisiblePosition == count - visibleThreshold && hasNextPage) {
            showLoading()
            callback.onMorePage(page)
        }
    }

    fun showLoading() {
        if (isMore && !isLoading) {
            isLoading = true
            handler.post { notifyItemChanged(getMorePosition()) }
        }
    }

    fun hideLoading() {
        if (isMore && isLoading) {
            isLoading = false
            handler.post { notifyItemChanged(getMorePosition()) }
        }
    }

    fun hideStopLoading() {
        hasNextPage = false
        hideLoading()
    }

    fun hideLoading(size: Int, unitPerPage: Int = 0) {
        if (size > 0) page++
        if (size < unitPerPage) hasNextPage = false
        hideLoading()
    }

    fun hideLoading(maxSize: Int) {
        if (getItemSize() >= maxSize) {
            hasNextPage = false
        } else {
            page++
        }
        hideLoading()
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
    }

    fun isHasNextPage(): Boolean = hasNextPage

    fun setPage(page: Int) {
        this.page = page
    }

    fun getPage(): Int = page

    //----------------------------------------------------------------------------------------------
    // More Holder
    //----------------------------------------------------------------------------------------------
    private inner class MoreHolder(parent: ViewGroup) : BaseViewHolder<Any>(parent, R.layout.adapter_more_footer) {

        init {
            if (!isAutoMore) {
                view.layout_more.setOnClickListener {
                    showLoading()
                    callback.onMorePage(page)
                }
            }
        }

        override fun bind(data: Any, position: Int) {
            if (hasNextPage) {
                if (isAutoMore) {
                    if (isLoading) {
                        view.layout_more.visibility = View.INVISIBLE
                        view.pb_loading.visibility = View.VISIBLE
                    } else {
                        view.layout_more.visibility = View.GONE
                        view.pb_loading.visibility = View.GONE
                    }
                } else {
                    if (page == 1) {
                        view.isEnabled = false
                        view.layout_more.visibility = View.INVISIBLE
                        view.pb_loading.visibility = View.VISIBLE
                    } else {
                        if (isLoading) {
                            view.isEnabled = false
                            view.layout_more.visibility = View.INVISIBLE
                            view.pb_loading.visibility = View.VISIBLE
                        } else {
                            view.isEnabled = true
                            view.layout_more.visibility = View.VISIBLE
                            view.pb_loading.visibility = View.GONE
                        }
                    }
                }
            } else {
                view.isEnabled = false
                view.layout_more.visibility = View.GONE
                view.pb_loading.visibility = View.GONE
            }
        }
    }
}