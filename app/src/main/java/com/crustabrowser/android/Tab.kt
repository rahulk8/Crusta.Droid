package com.crustabrowser.android

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_tab.view.*

class Tab @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var isCurrent = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tab, this)

        web_view.progressBar = progress_bar
    }
}