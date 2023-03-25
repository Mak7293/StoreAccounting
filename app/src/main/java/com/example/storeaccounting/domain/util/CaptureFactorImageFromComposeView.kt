package com.example.storeaccounting.domain.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import com.example.storeaccounting.presentation.add_edit_factor.SaleFactorUi

class CaptureFactorImageFromComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var list: List<Int> = emptyList()
    private var date: MutableState<String> = mutableStateOf("")
    private var showSign:  MutableState<Boolean> = mutableStateOf(false)
    private var modifier: MutableState<Modifier> = mutableStateOf(Modifier)
    private lateinit var context: Context

    constructor(
        list: List<Int>,
        date: MutableState<String>,
        showSign: MutableState<Boolean>,
        modifier: MutableState<Modifier>,
        context: Context
    ): this(context) {
        this.list = list
        this.date = date
        this.showSign = showSign
        this.modifier = modifier
        this.context = context
    }

    @Composable
    override fun Content() {
        // This is a ComposableUI function
        SaleFactorUi(
            list = list,
            date = date.value,
            showSign = showSign.value,
            modifier = modifier.value
        )
        Log.d("recompose","!!!!!")
    }

    fun capture(view: CaptureFactorImageFromComposeView): Bitmap {

        val bitmap = generateBitmap(view)
        return bitmap
    }
    fun generateBitmap(view: View):Bitmap{

        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(
            view.left,
            view.top,
            view.right,
            view.bottom
        )
        view.draw(canvas)
        return bitmap
    }

}