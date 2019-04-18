package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatRadioButton
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_poll.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Poll
import org.zapomni.venturers.domain.model.chat.PollAnswer
import org.zapomni.venturers.presentation.adapter.PollAnswerAdapter
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class PollDialog : BaseDialogFragment() {

    companion object {
        private val ARG_POLL = "ARG_POLL"

        fun newInstance(poll: Poll) = PollDialog()
                .apply {
                    arguments = Bundle().apply { putParcelable(ARG_POLL, poll) }
                }
    }

    var onCheckedAnswerListener: ((poll: PollAnswer) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_poll, null)
        setTypeFace()

        val poll = arguments!!.getParcelable<Poll>(ARG_POLL)

        with(mView) {
            btnFinish.setOnClickListener { dismiss() }
            if (poll.voted) showResult(poll)
            else showRadioGroup(poll)
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    private fun showRadioGroup(poll: Poll) {
        with(mView) {
            rbPoll.visibility = View.VISIBLE
            rvPollAnswer.visibility = View.GONE

            tvTitleTop.text = poll.question
            poll.answers.forEachIndexed { index, pollAnswer ->
                val radioButton = AppCompatRadioButton(context).apply {
                    id = index
                    text = pollAnswer.answer
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(context, R.color.mainTextLight))
                    typeface = regularTypeFace
                }
                rbPoll.addView(radioButton)
            }

            rbPoll.setOnCheckedChangeListener { _, checkedId ->
                onCheckedAnswerListener?.invoke(poll.answers[checkedId])
                showResult(poll)
            }
        }
    }

    private fun showResult(poll: Poll) {
        with(mView) {
            rvPollAnswer.visibility = View.VISIBLE
            rbPoll.visibility = View.GONE

            rvPollAnswer.adapter = PollAnswerAdapter().apply { setItems(poll.answers) }
        }
    }

    override fun setTypeFace() {
        with(mView) {
            tvPoll.typeface = boldTypeFace
            tvTitleTop.typeface = regularTypeFace
        }
    }
}