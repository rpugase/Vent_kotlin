package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import org.zapomni.venturers.extensions.dpToPx

class TriangleBorderView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val path = Path()
    private val paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val borderPath = Path()
    private val borderPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(229, 229, 229)
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val halfWidth = width / 2f
            val borderWidth = 1.dpToPx().toFloat()

            borderPath.moveTo(halfWidth, 0f)
            borderPath.lineTo(width.toFloat(), height.toFloat())
            borderPath.lineTo(0f, height.toFloat())

            path.moveTo(halfWidth, borderWidth)
            path.lineTo(width - borderWidth, height.toFloat())
            path.lineTo(borderWidth, height.toFloat())

            canvas.drawPath(borderPath, borderPaint)
            canvas.drawPath(path, paint)
        }
    }
}