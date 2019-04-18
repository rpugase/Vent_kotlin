package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ProgressBar
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.dpToPx

class PollProgressView(context: Context, attributeSet: AttributeSet) : ProgressBar(context, attributeSet) {

    private val progressPath = Path()
    private val progressPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.progressPoll)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (progress != 0 && progress != max) {
            progressPath.moveTo(width.toFloat() / max, 0f)
            val rad = 6.dpToPx().toFloat()
            progressPath.addRoundRect(0f, 0f, rad, height.toFloat(), rad, rad, Path.Direction.CW)
            canvas?.drawPath(progressPath, progressPaint)
            canvas?.clipPath(progressPath)
        }
    }
}