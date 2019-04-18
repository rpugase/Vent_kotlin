package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.flip_clock.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.log
import java.util.concurrent.TimeUnit

class FlipClockView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private var boldTypeFace: Typeface? = ResourcesCompat.getFont(context, R.font.gotha_pro_bold)
    private var regularTypeFace: Typeface? = ResourcesCompat.getFont(context, R.font.source_sans_pro_regular)

    private val flipClock = LayoutInflater.from(context).inflate(R.layout.flip_clock, null)
    private var disposable = Disposables.disposed()
    private var from = 0L
    private var lastFrom = 0L

    init {
        addView(flipClock)

        tvDaysFirst.typeface = boldTypeFace
        tvDaysSecond.typeface = boldTypeFace
        tvHoursFirst.typeface = boldTypeFace
        tvHoursSecond.typeface = boldTypeFace
        tvMinutesFirst.typeface = boldTypeFace
        tvMinutesSecond.typeface = boldTypeFace

        tvDays.typeface = regularTypeFace
        tvHours.typeface = regularTypeFace
        tvMinutes.typeface = regularTypeFace
    }

    override fun onDetachedFromWindow() {
        if (!disposable.isDisposed) disposable.dispose()
        super.onDetachedFromWindow()
    }

    fun start(from: Long) {
        this.from = Math.max(0, from - System.currentTimeMillis())
        drawTimer()

        if (this.from == 0L) {
            return
        }

        if (!disposable.isDisposed) disposable.dispose()
        disposable = Observable.interval(1, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    lastFrom = if (lastFrom == 0L) from else this.from
                    this.from = this.from - DateUtils.MINUTE_IN_MILLIS
                    drawTimer()
                }
    }

    private fun drawTimer() {
        val minutes = from / DateUtils.MINUTE_IN_MILLIS % 60
        val hours = from / DateUtils.HOUR_IN_MILLIS % 24
        val days = from / DateUtils.DAY_IN_MILLIS

        drawDays(days)
        drawHours(hours)
        drawMinutes(minutes)
    }

    private fun drawDays(days: Long) {
        log("Days $days")
        val daysArr = days.toString().toCharArray()
        if (daysArr.size == 2) {
            tvDaysFirst.text = daysArr[0].toString()
            tvDaysSecond.text = daysArr[1].toString()
        } else if (daysArr.size == 1) {
            tvDaysFirst.text = "0"
            tvDaysSecond.text = daysArr[0].toString()
        }
    }

    private fun drawHours(hours: Long) {
        log("Hours $hours")
        val hoursArr = hours.toString().toCharArray()
        if (hoursArr.size == 2) {
            tvHoursFirst.text = hoursArr[0].toString()
            tvHoursSecond.text = hoursArr[1].toString()
        } else if (hoursArr.size == 1) {
            tvHoursFirst.text = "0"
            tvHoursSecond.text = hoursArr[0].toString()
        }
    }

    private fun drawMinutes(minutes: Long) {
        log("Minutes $minutes")
        val minutesArr = minutes.toString().toCharArray()
        if (minutesArr.size == 2) {
            tvMinutesFirst.text = minutesArr[0].toString()
            tvMinutesSecond.text = minutesArr[1].toString()
        } else if (minutesArr.size == 1) {
            tvMinutesFirst.text = "0"
            tvMinutesSecond.text = minutesArr[0].toString()
        }
    }
}