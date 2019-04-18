package org.zapomni.venturers.presentation.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.extensions.spToPx

class BottomBarView(context: Context, attributeSet: AttributeSet) : RadioGroup(context, attributeSet) {

    val buttons = arrayOf(
            Radio(0, R.drawable.selector_kangaroo, context.getString(R.string.main), true),
            Radio(1, R.drawable.selector_chat, context.getString(R.string.chat)),
            Radio(2, R.drawable.selector_flag, context.getString(R.string.events)),
            Radio(3, R.drawable.selector_flame, context.getString(R.string.adventure))
    )

    var onCheckedChangeListener: ((position: Int) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(0)
        }

    init {
        orientation = LinearLayout.HORIZONTAL
        buttons.forEach(this::addView)

        setOnCheckedChangeListener { _, checkedId ->
            buttons.forEach { it.checkedState = it.id == checkedId }
            onCheckedChangeListener?.invoke(checkedId)
        }
    }

    inner class Radio(viewId: Int, drawableRes: Int, val text: String, checked: Boolean = false) : RadioButton(context) {

        var checkedState = checked
            set(value) {
                field = value
                setText(if (value) text else null)
                textSize = if (value) 8.spToPx().toFloat() else 6f
            }

        init {
            isChecked = checked
            id = viewId
            val padding = 8.dpToPx()
            setPadding(padding, padding, padding, padding)
            gravity = Gravity.CENTER
            layoutParams = RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f)
            setCompoundDrawablesWithIntrinsicBounds(null, context.getDrawable(drawableRes), null, null)
            buttonDrawable = ColorDrawable(Color.TRANSPARENT)
            background = ColorDrawable(Color.TRANSPARENT)
        }
    }
}