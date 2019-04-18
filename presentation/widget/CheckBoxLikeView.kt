package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.zapomni.venturers.R

class CheckBoxLikeView(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    var onCheckedChangeListener: ((isChecked: Boolean) -> Unit)? = null
    var checked: Boolean
        get() = cb.isChecked
        set(value) {
            cb.isChecked = value
        }
    var countLikes: Int
        get() = if (tv.text.isEmpty()) 0 else tv.text.toString().toInt()
        set(value) {
            tv.visibility = if (value == 0) View.GONE else View.VISIBLE
            tv.text = value.toString()
        }

    private var tv: TextView
    private var cb: AppCompatCheckBox

    init {
        orientation = VERTICAL
        cb = AppCompatCheckBox(context).apply {
            setButtonDrawable(R.drawable.checkbox_like)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) countLikes++
                else countLikes--
            }
            setOnClickListener { onCheckedChangeListener?.invoke(isChecked) }
        }
        tv = AppCompatTextView(context).apply {
            setTextColor(ContextCompat.getColor(context, R.color.mainTextLight))
            gravity = Gravity.CENTER
            setOnClickListener { onCheckedChangeListener?.invoke(cb.isChecked) }
        }

        addView(cb)
        addView(tv)


    }
}