package com.mgkim.sample.ui.base.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mgkim.sample.R

/**
 * 일반 Dialog Base class
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 7:07
 **/
open class CommonDialog : Dialog, LifecycleObserver {

    lateinit var activity: AppCompatActivity

    constructor(context: Context) : this(context, 0)

    constructor(context: Context, layout: Int) : this(context, R.style.Full_Dialog, layout)

    constructor(context: Context, style: Int, layout: Int) : super(context, style) {
        if (context is AppCompatActivity) {
            context.lifecycle.addObserver(this)
            activity = context
        }

        setContentView(layout)
        setCanceledOnTouchOutside(false)
    }

    fun setShowAnimation(anim: Int) {
        window?.setWindowAnimations(anim)
    }

    fun setFullScreen(activity: Activity) {
        val windowSize = Point()
        activity.windowManager.defaultDisplay.apply {
            getSize(windowSize)
        }
        this.window?.apply {
            setLayout(windowSize.x, windowSize.y - getStatusBarHeight(activity))
        }
    }

    private fun getStatusBarHeight(activity: Activity): Int {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        return rect.top
    }

    override fun show() {
        if (activity?.isFinishing) {
            return
        }
        super.show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun close() {
        if (isShowing) {
            dismiss()
        }
    }
}