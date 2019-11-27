package com.mgkim.sample

import com.mgkim.sample.di.component.DaggerAppComponent
import com.mgkim.libs.webimageview.NetManager
import com.mgkim.libs.webimageview.NetManagerConfig
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SampleTestApp: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        //NetManager init
        NetManager.init(this, NetManagerConfig(
            NetManagerConfig.WebImageViewConfig(
//                diskCacheOption = NetManagerConfig.DiskCacheOption.ALL_DISK_CACEH,
//                isMemoryCache = true,
//                preferredConfig = Bitmap.Config.ARGB_8888,
                defaultImageResId = R.drawable.ic_default_picture,
                failImageResId = R.drawable.ic_frown,
                animResId = android.R.anim.fade_in
//                progressResId = R.drawable.progress_call,
//                isResize = true,
//                isBigSize = false
            )
        ))
    }
}