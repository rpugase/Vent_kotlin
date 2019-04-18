package org.zapomni.venturers.presentation.event

import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseView

interface EventView : BaseView {

    fun setEvent(event: Event)

    fun showDays(days: List<String>)

    fun showPhotos(photos: List<String>, startPosition: Int)

    fun showHeadImage(imgUrl: String, needToAnimate: Boolean)

    fun prefetchImages(images: List<String>)
}