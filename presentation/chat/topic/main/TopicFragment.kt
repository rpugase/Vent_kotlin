package org.zapomni.venturers.presentation.chat.topic.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_new_topic.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Agenda
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.domain.model.chat.Poll
import org.zapomni.venturers.extensions.*
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.chat.topic.TopicActivity
import org.zapomni.venturers.presentation.chat.topic.background.BackgroundTopicFragment
import java.io.File

class TopicFragment : BaseFragment<TopicView, TopicPresenter>(), TopicView {
    companion object {
        fun newInstance() = TopicFragment().apply {
            arguments = Bundle()
        }
    }

    private val options = mutableListOf<EditText>()
    private var titleWatcher: TextWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnAddOption.setOnClickListener { addAnswerOption() }
        btnRemoveOption.setOnClickListener { removeAnswerOption() }
        btnSetBackground.setOnClickListener { BackgroundTopicFragment.newInstance().add(fragmentManager, true) }

        spTopicOrPoll.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.spinnerState = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    override fun onStart() {
        super.onStart()
        titleWatcher = etTitle.watchText(presenter::setTitle)
    }

    override fun onPause() {
        etTitle.removeTextChangedListener(titleWatcher)
        super.onPause()
    }

    override fun setTitleTopic(text: String) {
        tvTitleTop.text = if (text.isEmpty()) getString(R.string.title_topic) else text
    }

    override fun setTitlePoll(text: String) {
        tvTitleTop.text = if (text.isEmpty()) getString(R.string.title_poll) else text
    }

    override fun titleError(resId: Int) {
        etTitle.requestFocus()
        etTitle.error = getString(resId)
    }

    override fun descriptionError(resId: Int) {
        etDescription.requestFocus()
        etDescription.error = getString(resId)
    }

    override fun answerOptionsError(index: Int, resId: Int) {
        options[index].apply {
            requestFocus()
            error = getString(resId)
        }
    }

    override fun showTopic() {
        llTopic.visibility = View.VISIBLE
        llPoll.visibility = View.GONE
        btnCreate.setOnClickListener { presenter.createTopic(etTitle.text.toString(), etDescription.text.toString()) }
        tvAgenda.setText(R.string.agenda)
        tvLearnMore.setText(R.string.learn_more)
        if (etTitle.text.toString().isEmpty()) tvTitleTop.text = getString(R.string.title_topic)
    }

    override fun showPoll() {
        llTopic.visibility = View.GONE
        llPoll.visibility = View.VISIBLE
        btnCreate.setOnClickListener { presenter.createPoll(etTitle.text.toString(), options.map { it.text.toString() }) }
        tvAgenda.setText(R.string.day_interview)
        tvLearnMore.setText(R.string.vote)
        if (etTitle.text.toString().isEmpty()) tvTitleTop.text = getString(R.string.title_poll)
    }

    override fun addAnswerOption() {
        options.add((layoutInflater.inflate(R.layout.view_answer_option, null) as EditText))
        llAnswerOptions.addView(options.last())
        options.last().apply {
            hint = getString(R.string.variant, options.size)
            layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                setSingleLine()
                setMargins(0, 12.dpToPx(), 0, 0)
                typeface = regularTypeFace
            }
        }

        if (options.size > 2) btnRemoveOption.visibility = View.VISIBLE
        if (options.size == 8) btnAddOption.visibility = View.INVISIBLE
    }

    override fun removeAnswerOption() {
        llAnswerOptions.removeView(options.last())
        options.removeAt(options.lastIndex)

        if (options.size == 2) btnRemoveOption.visibility = View.GONE
        if (options.size < 8) btnAddOption.visibility = View.VISIBLE
    }

    override fun goBack(agenda: Agenda) {
        activity?.setResult(Activity.RESULT_OK, Intent().putExtra(TopicActivity.EXTRA_AGENDA, agenda))
        activity?.finish()
    }

    override fun goBack(poll: Poll) {
        activity?.setResult(Activity.RESULT_OK, Intent().putExtra(TopicActivity.EXTRA_POLL, poll))
        activity?.finish()
    }

    override fun showBackgroundModel(backgroundModel: BackgroundModel) {
        if (backgroundModel.drawable != null) imgBackground.loadImageFromAssets(backgroundModel.id)
        else imgBackground.loadImageFromAssets(backgroundModel.id)
        presenter.pictureId = backgroundModel.id
    }

    override fun showBackgroundUri(uri: Uri?) {
        if (uri != null) {
            imgBackground.loadImage(uri)
            presenter.pictureFile = File(context?.getRealPathFromUri(uri))
        }
    }

    override fun setTypeFace() {
        btnSetBackground.typeface = semiBoldTypeFace
        tvTopicOrPoll.typeface = semiBoldTypeFace
        tvTitle.typeface = semiBoldTypeFace
        etTitle.typeface = regularTypeFace
        tvAnswerOptions.typeface = semiBoldTypeFace
        tvDescription.typeface = semiBoldTypeFace
        etDescription.typeface = regularTypeFace
        btnCreate.typeface = boldTypeFace
        tvAgenda.typeface = boldTypeFace
        tvTitleTop.typeface = regularTypeFace
        tvLearnMore.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_new_topic
    override fun createPresenter() = inject<TopicPresenter>().value
}