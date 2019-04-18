package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import org.zapomni.venturers.R

class TopBackgroundView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply { style = Paint.Style.FILL }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(width / 2f, -width / 2f, width.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec / 2)
    }
}