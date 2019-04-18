package org.zapomni.venturers.presentation.chat.topic.background

import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.presentation.base.BaseView

interface BackgroundTopicView : BaseView {

    fun loadBackgrounds(backgrounds: List<BackgroundModel>)
    fun openGallery()
    fun sendBackgroundModel(backgroundModel: BackgroundModel)
}