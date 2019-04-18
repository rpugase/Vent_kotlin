package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TriangleView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val path = Path()
    private val paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val halfWidth = width / 2f

            path.moveTo(halfWidth, 0f)
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())

            canvas.drawPath(path, paint)
        }
    }
}