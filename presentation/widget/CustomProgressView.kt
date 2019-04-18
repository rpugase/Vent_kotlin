package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ProgressBar
import org.zapomni.venturers.R

class CustomProgressView(context: Context, attributeSet: AttributeSet) : ProgressBar(context, attributeSet) {

    private val progressPath = Path()
    private val progressPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.progress)
    }

    private val points by lazy {
        arrayOf(Point(width * (progress.toFloat() / max) + (1f / 25f) * width, 0f),
                Point(width * (progress.toFloat() / max), height.toFloat()),
                Point(getOffset(), height.toFloat()))

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (progress >= getOffsetIteration().toInt() && progress < max - getOffsetIteration()) {
            progressPath.moveTo(getOffset(), 0f)
            points.forEach { progressPath.lineTo(it.x, it.y) }
            canvas?.drawPath(progressPath, progressPaint)
            canvas?.clipPath(progressPath)
        }
    }

    inner class Point(val x: Float, val y: Float) {
        override fun toString() = "$x $y"
    }

    private fun getOffset() = width * (getOffsetIteration() / max)

    private fun getOffsetIteration() = when (max) {
        50 -> 3f
        25 -> 2f
        else -> 1f
    }
}