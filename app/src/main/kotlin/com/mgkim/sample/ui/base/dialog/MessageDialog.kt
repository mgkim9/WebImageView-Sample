package com.mgkim.sample.ui.base.dialog

import android.app.Activity
import android.view.View
import com.mgkim.sample.R
import kotlinx.android.synthetic.main.dialog_message.*

/**
 * 단순히 Message만 노출하는 Dialog
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 7:08
 **/
open class MessageDialog(activity: Activity) : CommonPopupDialog(activity, R.layout.dialog_message) {

    fun setContent(content: String) {
        tv_content_only?.text = content
        tv_content?.text = content
    }

    fun setTitleAndContent(title: String, content: String) {
        layout_title_content?.visibility = View.VISIBLE
        tv_content_only?.visibility = View.GONE
        tv_title?.text = title
        tv_content?.text = content
    }

    fun setTitleAndContent(gravity: Int, title: String, content: CharSequence) {
        layout_title_content?.visibility = View.VISIBLE
        tv_content_only?.visibility = View.GONE
        tv_title?.text = title
        tv_content?.text = content
        layout_title_content.gravity = gravity
        tv_title?.gravity = gravity
        tv_content?.gravity = gravity
    }
}
