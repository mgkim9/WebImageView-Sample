package com.mgkim.sample.ui.imagesearch.view

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.mgkim.sample.R
import com.mgkim.sample.interfaces.ListMoreActionListener
import com.mgkim.sample.network.dto.kakao.KImageDto
import com.mgkim.sample.network.dto.kakao.KakaoResult
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveObserver
import com.mgkim.sample.ui.base.view.BaseFragment
import com.mgkim.sample.ui.base.view.BaseRecyclerListAdapter
import com.mgkim.sample.ui.imagesearch.viewmodel.SearchImageViewModel
import com.mgkim.sample.utils.Log
import kotlinx.android.synthetic.main.fragment_search_image.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchImageFragment : BaseFragment(), View.OnClickListener , BaseRecyclerListAdapter.OnListInteractionListener<KImageDto> {
    private val PAGE_LOAD_COUNT = 50
    private val columnCount = 4
    private var page = 1
    lateinit var toolsImageAdapter: SearchImageAdapter
    private lateinit var layoutManager:LinearLayoutManager

    @Inject
    lateinit var imageViewModel: SearchImageViewModel

    override fun getViewModel(): BaseViewModel? = imageViewModel

    override fun getLayoutId(): Int = R.layout.fragment_search_image

    override fun initView() {
        retainInstance = true
        RxView.clicks(btnSearch).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
            onClick(btnSearch)
        }

        etSearch.setOnEditorActionListener { v, keyCode, event ->
            when (keyCode) {
                EditorInfo.IME_ACTION_SEARCH -> search()
            }
            true
        }

        toolsImageAdapter = SearchImageAdapter(this@SearchImageFragment).apply {
            setMoreActionListener(object : ListMoreActionListener {
                override fun onMorePage(pageNum: Int) {
                    page = pageNum
                    (btnSearch.tag as? String)?.let {
                        imageViewModel.reqImages(it, null, pageNum, PAGE_LOAD_COUNT)
                    }
                }
            }, true, columnCount * 2)
        }

        layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (toolsImageAdapter.getItemViewType(position) < 0) {
                            columnCount
                        } else {
                            1
                        }
                    }
                }
            }
        }

        with(rv_list) {
            layoutManager = this@SearchImageFragment.layoutManager
            adapter = toolsImageAdapter
        }
    }

    override fun dataBinding() {
        imageViewModel.images.observe(this, object : LiveObserver<KakaoResult<List<KImageDto>>>() {
            override fun onSuccess(data: KakaoResult<List<KImageDto>>?) {
                if (data == null) {

                } else {
                    val curSize = toolsImageAdapter.getItemSize()
                    val list: ArrayList<KImageDto> = ArrayList()
                    data.documents.forEach {
                        //Webimage vs Glide 를 테스트하기위해 2개씩 Add
                        list.add(it)
                        list.add(it)
                    }
                    toolsImageAdapter.addItems(list)
                    toolsImageAdapter.hideLoading(data.meta.total_count)
                    rv_list.adapter?.notifyItemRangeInserted(curSize, list.size)
                }
            }

            override fun onError(
                error: Throwable,
                retryCallback: (() -> Unit?)?,
                cancelCallback: (() -> Unit?)?
            ) {
                Log.d(TAG, "ERROR : $error")
            }

            override fun onLoading(loading: Boolean) {}
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if(columnCount > 1) {
            val columnCount = if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) columnCount else columnCount * 2
            with(rv_list) {
                (layoutManager as? GridLayoutManager)?.apply {
                    spanCount = columnCount
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (toolsImageAdapter.getItemViewType(position) < 0) {
                                columnCount
                            } else {
                                1
                            }
                        }
                    }
                }
            }
        }
        super.onConfigurationChanged(newConfig)
    }

    private fun search() {
        if(etSearch.text.toString().isNullOrEmpty()) {
            Toast.makeText(activity, "검색어를 입력하세요.", Toast.LENGTH_LONG).show()
            return
        }

        page = 1
        btnSearch.tag = etSearch.text.toString()
        toolsImageAdapter.clearItems()
        imageViewModel.reqImages(etSearch.text.toString(), null, page, PAGE_LOAD_COUNT)
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, 0)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnSearch -> search()
        }
    }

    override fun onListInteraction(item: KImageDto) {
        Toast.makeText(activity, "item : $item", Toast.LENGTH_LONG).show()
    }
}