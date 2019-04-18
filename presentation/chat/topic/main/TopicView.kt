package org.zapomni.venturers.presentation.chat.topic.main

import android.net.Uri
import android.support.annotation.StringRes
import org.zapomni.venturers.domain.model.chat.Agenda
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.domain.model.chat.Poll
import org.zapomni.venturers.presentation.base.BaseView

interface TopicView : BaseView {

    fun showTopic()
    fun showPoll()
    fun setTitleTopic(text: String)
    fun setTitlePoll(text: String)

    fun addAnswerOption()
    fun removeAnswerOption()

    fun goBack(agenda: Agenda)
    fun goBack(poll: Poll)

    fun showBackgroundModel(backgroundModel: BackgroundModel)
    fun showBackgroundUri(uri: Uri?)

    fun titleError(@StringRes resId: Int)
    fun descriptionError(@StringRes resId: Int)
    fun answerOptionsError(index: Int, @StringRes resId: Int)
}