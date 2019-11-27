package com.mgkim.sample.ui.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mgkim.sample.R
import kotlinx.android.synthetic.main.dialog_common_popup.*

/**
 * 버튼있는 Dialog
 * @author : mgkim
 * @version : 1.0.0
 * @since : 2019-11-27 오후 7:07
 **/
open class CommonPopupDialog : CommonDialog {

    constructor(context: Context, layout: Int) : this(context, R.style.Popup_Dialog, layout)

    constructor(context: Context, style: Int, layout: Int) : super(context, style, R.layout.dialog_common_popup) {
        view_content.addView(LayoutInflater.from(context).inflate(layout, view_content, false))
        init()
    }

    open fun init() {

    }

    fun setPositiveButton(clickListener: View.OnClickListener?, string: String = String()) {
        btn_positive.setOnClickListener(clickListener ?: View.OnClickListener { dismiss() })
        if (string.isNotBlank()) {
            btn_positive?.text = string
        }
    }

    fun setNegativeButton(clickListener: View.OnClickListener?, string: String = String()) {
        btn_negative?.visibility = View.VISIBLE
        btn_negative?.setOnClickListener(clickListener ?: View.OnClickListener { dismiss() })
        if (string.isNotBlank()) {
            btn_negative.text = string
        }
    }
}