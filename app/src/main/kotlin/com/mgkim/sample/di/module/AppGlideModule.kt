package com.mgkim.sample.di.module

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.engine.executor.GlideExecutor.newDiskCacheExecutor
import com.bumptech.glide.load.engine.executor.GlideExecutor.newSourceExecutor
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.mgkim.sample.R


@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        val sourceExecutor = newSourceExecutor(3, "source", GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
        val diskExecutor = newDiskCacheExecutor(3, "disk-cache", GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
        builder.setSourceExecutor(sourceExecutor)
            .setDiskCacheExecutor(diskExecutor)
            .setDefaultRequestOptions(
                RequestOptions().fallback(R.drawable.ic_frown)
                    .placeholder(R.drawable.ic_default_picture)
            )

    }
}