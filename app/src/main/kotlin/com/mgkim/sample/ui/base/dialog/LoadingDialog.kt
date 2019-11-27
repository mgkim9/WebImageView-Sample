package com.mgkim.sample.ui.base.dialog

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mgkim.sample.R

/**
 * Loading용 Dialog
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 7:08
 **/
class LoadingDialog(context: Context) : Dialog(context, R.style.Loading_Dialog), LifecycleObserver {

    init {
        if (context is AppCompatActivity) {
            context.lifecycle.addObserver(this)
        }

        setContentView(R.layout.layout_loading_dialog)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun close() {
        if (isShowing) {
            dismiss()
        }
    }
}