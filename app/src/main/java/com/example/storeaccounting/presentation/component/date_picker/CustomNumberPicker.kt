package com.example.storeaccounting.presentation.component.date_picker

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


class CustomNumberPicker : NumberPicker {
/*
    var typeface: Typeface? = null
    var textSize: Float? = null
    var textColor: Int? = null
*/


    constructor(context: Context?) : super(context!!) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {

    }


    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(
        child: View, index: Int,
        params: ViewGroup.LayoutParams
    ){
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is EditText) {
           // if (typeface != null) view.typeface = typeface
            if (GlobalStyle.textSize != null) view.textSize = GlobalStyle.textSize!!
            if (GlobalStyle.textColor != null) view.setTextColor(GlobalStyle.textColor!!)
        }

    }
}
internal object GlobalStyle {

    internal var typeface: Typeface? = null
    internal var textSize: Float? = null
    internal var textColor: Int? = null

}