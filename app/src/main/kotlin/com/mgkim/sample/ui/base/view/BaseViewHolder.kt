package com.mgkim.sample.ui.base.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<D>(parent: ViewGroup, @LayoutRes resId: Int, val isClickable: Boolean = true)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(resId, parent, false)) {

    var context: Context = itemView.context
    var view = itemView

    abstract fun bind(data: D, position: Int)

    open fun changed(data: D, position: Int, payloads: List<Any>) {}

    open fun unbind() {}

    protected fun getString(@StringRes resId: Int): String = context.getString(resId)

}