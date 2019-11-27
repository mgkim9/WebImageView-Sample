package com.mgkim.sample.ui.gallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mgkim.sample.constants.GETTY_URL
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.LiveWrapper
import com.mgkim.libs.webimageview.IDoInBackground
import com.mgkim.libs.webimageview.IRequest
import com.mgkim.libs.webimageview.IResultReceiver
import com.mgkim.libs.webimageview.RequestLocal
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.*


class GalleryViewModel : BaseViewModel() {
    private var mutableImages = MutableLiveData<LiveWrapper<List<String>>>()
    val images: LiveData<LiveWrapper<List<String>>> get() = mutableImages

    fun reqImages() {
        val wrapper = LiveWrapper<List<String>>()
        mutableLoadingSubject.value = true
        RequestLocal<List<String>>()
            .setDoInBackground {
                imageParser(GETTY_URL)
            }.setReceiver { isSuccess, obj ->
                if (isSuccess) {
                    mutableImages.value = wrapper.success(obj.getResult())
                } else {
                    mutableImages.value = wrapper.error(throw Throwable(), fun() {
                        reqImages()
                    })
                }
                mutableLoadingSubject.value = false
            }.useHandler() //결과(onResult)를 mainThread에서 수행 함
            .addReq()
    }


    private fun imageParser(url:String) :ArrayList<String> {
        val images: ArrayList<String> = ArrayList()
        val response = Jsoup.connect(url).method(Connection.Method.GET).execute()
        val grids = response.parse().body().getElementsByClass("grid masonry-grid masonry-view")[0]
        val imageItems = grids.getElementsByTag("img")
        for (item in imageItems) {
            val imageUrl = item.attr("data-src")
            if(!imageUrl.isNullOrEmpty()) {
                images.add(imageUrl)
            }
        }
        return images
    }
}