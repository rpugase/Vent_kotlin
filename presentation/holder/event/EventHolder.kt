package org.zapomni.venturers.presentation.holder.event

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.item_event.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.presentation.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class EventHolder(root: ViewGroup, private val onItemClickListener: ((event: Event) -> Unit)? = null) : BaseViewHolder<Event>(root, R.layout.item_event) {
    override fun bind(item: Event) {
        with(itemView) {
            tvTitle.typeface = boldTypeFace
            tvDate.typeface = regularTypeFace
            tvDayOfWeek.typeface = regularTypeFace
            tvTime.typeface = regularTypeFace
            tvMonth.typeface = boldTypeFace

            tvTitle.text = item.title
            imgEvent.controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(imgEvent.controller)
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(item.headImages.first()))
                            .setResizeOptions(ResizeOptions(640, 130.dpToPx()))
                            .build())
                    .build()
            tvDate.text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(item.date.startDate)
            tvDayOfWeek.text = SimpleDateFormat("EEEE", Locale.getDefault()).format(item.date.startDate)
            tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(item.date.startDate)

            tvMonth.text = context.resources.getStringArray(R.array.months)[Calendar.getInstance().apply { time = item.date.startDate }.get(Calendar.MONTH)]
            tvMonth.visibility = if (item.needShowDate) View.VISIBLE else View.GONE

            setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }
}