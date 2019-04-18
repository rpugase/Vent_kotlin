package org.zapomni.venturers.presentation.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.dialog_ban_for_day.*
import kotlinx.android.synthetic.main.fragment_chat.*
import org.jetbrains.anko.contentView
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.chat.Agenda
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.domain.model.chat.Poll
import org.zapomni.venturers.extensions.*
import org.zapomni.venturers.presentation.adapter.ChatAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.chat.meet.MeetActivity
import org.zapomni.venturers.presentation.chat.topic.TopicActivity
import org.zapomni.venturers.presentation.dialog.*
import org.zapomni.venturers.presentation.dialog.tips.ChatFirstTip
import org.zapomni.venturers.presentation.main.MainActivity
import java.io.File

class ChatFragment : BaseFragment<ChatView, ChatPresenter>(), ChatView {

    private var adapter: ChatAdapter? = null
    private var tip: ChatFirstTip? = null

    private var keyboardHeight = 0
    private var keyboardVisible = false

    private var oldHeight = 0
    private var initOldHeight = false

    private var keyboardListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardBanForDay.visibility = View.GONE
        fabNewMessage.setOnClickListener { showKeyboard(etAnswer); presenter.showTip() }
        btnAnswer.setOnClickListener { presenter.sendMessage(etAnswer.text.toString()) }
        swTopic.addSwitchObserver { _, isChecked -> presenter.onTopic = isChecked }
        tvPhoto.setOnClickListener {
            hideKeyBoard()
            activity?.pickImageFromGallery {
                rlAnswer.visibility = View.VISIBLE
                showKeyboard(etAnswer)
                flImage.visibility = View.VISIBLE
                imgAnswer.loadImage(it)
                presenter.imgFile = File(context?.getRealPathFromUri(it))

                if (presenter.meet != null) {
                    presenter.meet = null
                    flMeet.visibility = View.GONE
                }
            }
        }
        tvMeet.setOnClickListener {
            //            rlAnswer.layoutParams = (rlAnswer.layoutParams as RelativeLayout.LayoutParams)
//                    .apply { setMargins(0, 0, 0, 0) }
            MeetActivity.startNewMeet(activity)
        }
        btnCloseImage.setOnClickListener {
            presenter.imgFile = null
            flImage.visibility = View.GONE
        }
        btnCloseMeet.setOnClickListener {
            presenter.meet = null
            flMeet.visibility = View.GONE
        }
        btnCloseReply.setOnClickListener {
            presenter.replyMessage = null
            rlReply.visibility = View.GONE
        }
        btnSwapChat.setOnClickListener {
            presenter.swapChat()
        }
        tvNameReply.requestFocus()
        tvMessageReply.requestFocus()
        rlReply.visibility = View.GONE
        rlAnswer.visibility = View.GONE
        rvChat.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                .apply { setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_chat)!!) })
        btnRemoveTopic.setOnClickListener { presenter.removeTopic() }
    }

    override fun onPause() {
        (activity as MainActivity).showNavBottom()
        clearKeyboardListener()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        setKeyboardListenerEvent()
    }

    override fun showTip() {
        tip = ChatFirstTip(activity as MainActivity, 54.dpToPx(), activity!!.contentView!!.findViewById<View>(R.id.navBottom).height) {
            tip = null
            showKeyboard(etAnswer)
        }
        tip?.show()
    }

    override fun showReplyMessage(message: Message) {
        ReplyDialog.newInstance(fragmentManager, message).apply {
            onOpenPhotoListener = this@ChatFragment::openPhoto
            onOpenReplyMessageListener = presenter::showReplyMessage
        }
    }

    override fun setVisibilitySwapChat(show: Boolean) {
        btnRemoveTopic.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun loadMeets(meets: List<Message>) {
        activity?.runOnUiThread {
            flMeets.visibility = View.VISIBLE
            flMeets.setOnClickListener { MeetActivity.startMeets(activity, meets.toTypedArray()) }
            val countMeets = meets.size
            tvMeetsCount.text = if (countMeets > 10) "+10" else countMeets.toString()
        }
    }

    override fun loadPoll(poll: Poll) {
        llTop.visibility = View.VISIBLE
        llTop.setOnClickListener { presenter.openPoll() }
        root.setPadding(0, 0, 0, 0)

        tvLearnMore.visibility = View.VISIBLE
        tvChatEvent.visibility = View.GONE
        tvAgenda.setText(R.string.day_interview)
        tvTitleTop.text = poll.question

        if (poll.photoId != null) {
            if (context!!.assets.list("chat_backgrounds").contains(poll.photoId + ".png"))
                imgBackground.loadImageFromAssets(poll.photoId + ".png")
            else
                imgBackground.loadImage(poll.photoId.getPhotoUrlById())
        }
    }

    override fun loadAgenda(agenda: Agenda) {
        llTop.visibility = View.VISIBLE
        llTop.setOnClickListener { TopicDialog.newInstance(agenda).show(fragmentManager) }
        root.setPadding(0, 0, 0, 0)

        tvLearnMore.visibility = View.VISIBLE
        tvChatEvent.visibility = View.GONE
        tvAgenda.setText(R.string.agenda)
        tvTitleTop.text = agenda.theme

        tvMeet.text = null
        tvPhoto.text = null
        swTopic.visibility = View.VISIBLE
        tvOnTopic.visibility = View.VISIBLE
        if (agenda.photoId != null) {
            if (context!!.assets.list("chat_backgrounds").contains(agenda.photoId))
                imgBackground.loadImageFromAssets(agenda.photoId)
            else
                imgBackground.loadImage(agenda.photoId.getPhotoUrlById())
        }
    }

    override fun loadAgendaForEvent(agenda: Agenda) {
        llTop.visibility = View.VISIBLE

        tvLearnMore.visibility = View.GONE
        tvChatEvent.visibility = View.VISIBLE
        tvAgenda.text = agenda.theme
        tvTitleTop.text = null
    }

    override fun loadChat(messages: List<Message>) {
        adapter = ChatAdapter(presenter)
        rvChat.adapter = adapter
        adapter?.setItems(messages)
    }

    override fun hidePoll() {
        root.setPadding(0, getStatusBarHeight(), 0, 0)
        llTop.visibility = View.GONE
    }

    override fun hideAgenda() {
        root.setPadding(0, getStatusBarHeight(), 0, 0)
        llTop.visibility = View.GONE
    }

    override fun hideMeets() {
        flMeets.visibility = View.GONE
    }

    override fun clearChat() {
        adapter?.setItems(null)
    }

    override fun showAnswerButton(show: Boolean) {
        btnAnswer.visibility = if (show) View.VISIBLE else View.INVISIBLE
        progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    override fun showUser(user: User, canSeePhone: Boolean, showPhone: Boolean) {
        val dialog = UserDialog.newInstance(fragmentManager, user, canSeePhone, showPhone)
        dialog.onShowPhoneListener = { presenter.onShowPhone(user) }
    }

    override fun showPoll(poll: Poll) {
        PollDialog.newInstance(poll).apply {
            onCheckedAnswerListener = presenter::sendPollAnswer
        }.show(fragmentManager)
    }

    override fun showReply(message: Message) {
        rlReply.visibility = View.VISIBLE
        tvNameReply.text = message.user.name
        tvMessageReply.text = message.text
    }

    override fun showTopicButton() {
        fabNewTopic.visibility = View.VISIBLE
        fabNewTopic.setOnClickListener { TopicActivity.start(activity) }
    }

    override fun updateChest(index: Int, message: Message) {
        adapter?.changeItem(index, message)
    }

    override fun addMessage(message: Message) {
        adapter?.addItem(message)
        rvChat.smoothScrollToPosition(adapter?.items!!.lastIndex)
    }

    override fun removeMessage(message: Message) {
        adapter?.removeItem(message)
    }

    override fun changeMessage(message: Message) {
        adapter?.changeItem(message)
    }

    override fun onMessageAction(message: Message, showPhone: Boolean, admin: Boolean) {
        val dialog = MessageActionDialog.newInstance(showPhone, admin)
        dialog.show(fragmentManager)
        dialog.onReplyListener = {
            fabNewMessage.requestFocus()
            fabNewMessage.performClick()
            presenter.replyMessage(message)
        }
        dialog.onShowPhoneListener = { presenter.onUserShow(message.user) }
        if (admin) {
            val reasonDialog = ReasonBanDialog.newInstance(message.user)
            val permanentBanDialog = PermanentBanDialog.newInstance(message.user)
            reasonDialog.onBanListener = { presenter.banForDay(message.user, it) }
            permanentBanDialog.onBanListener = { presenter.banPermanent(message.user) }

            dialog.onDeleteMessageListener = { presenter.removeMessage(message) }
            dialog.onBanDayListener = { reasonDialog.show(fragmentManager) }
            dialog.onBanAlwaysListener = { permanentBanDialog.show(fragmentManager) }
        }
    }

    override fun onStartSendingMessage() {
        btnAnswer.visibility = View.INVISIBLE
        btnAnswer.isClickable = false
    }

    override fun onFinishSendingMessage(allRight: Boolean) {
        btnAnswer.visibility = View.VISIBLE
        btnAnswer.isClickable = true

        if (allRight) {
            etAnswer.text = null
            flImage.visibility = View.GONE
            flMeet.visibility = View.GONE
            rlReply.visibility = View.GONE
        }
    }

    override fun addMessages(messages: List<Message>) {
        adapter?.addItemsToTop(messages)
    }

    override fun startPagination() {
        adapter?.onLoadNewMessageEvent = presenter::loadMessages
    }

    override fun stopPagination() {
        adapter?.onLoadNewMessageEvent = null
    }

    @SuppressLint("SetTextI18n")
    override fun loadAvailableMessages(current: Int, max: Int) {
        tvCountItems.text = "$current/$max"
    }

    override fun showMessageBar() {
        fabNewMessage.visibility = View.GONE
        rlAnswer.visibility = View.VISIBLE
        rlAnswer.layoutParams = (rlAnswer.layoutParams as RelativeLayout.LayoutParams)
                .apply { setMargins(0, 0, 0, keyboardHeight) }
    }

    override fun showFab() {
        if (tip == null) {
            fabNewMessage.visibility = View.VISIBLE
            rlAnswer.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MeetActivity.REQ_NEW_MEET) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                flMeet.visibility = View.VISIBLE
                presenter.meet = data.getParcelableExtra(MeetActivity.EXTRA_MEET)

                if (presenter.imgFile != null) {
                    presenter.imgFile = null
                    flImage.visibility = View.GONE
                }
                presenter.sendMessage(etAnswer.text.toString())
            }
            rlAnswer.visibility = View.VISIBLE
            showKeyboard(etAnswer)
        } else if (requestCode == TopicActivity.REQ_TOPIC && resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra(TopicActivity.EXTRA_AGENDA)) {
                presenter.saveAgenda(data.getParcelableExtra(TopicActivity.EXTRA_AGENDA))
            } else if (data.hasExtra(TopicActivity.EXTRA_POLL)) {
                presenter.savePoll(data.getParcelableExtra(TopicActivity.EXTRA_POLL))
            }
        }
    }

    override fun openPhoto(photoUrl: String) {
        ImageViewer.Builder(context, arrayOf(photoUrl)).show()
    }

    override fun doOnBanForDay(reason: String) {
        cardBanForDay.visibility = View.VISIBLE
        showFab()
        hideKeyBoard()
        tvBanReason.text = reason
        fabNewMessage.visibility = View.GONE
        llTop.visibility = View.GONE
        rvChat.visibility = View.GONE
        btnSwapChat.visibility = View.GONE
    }

    override fun removeTopic() {
        llTop.visibility = View.GONE
    }

    override fun setTypeFace() {
        btnAnswer.typeface = boldTypeFace
        tvPhoto.typeface = semiBoldTypeFace
        tvMeet.typeface = semiBoldTypeFace
        etAnswer.typeface = regularTypeFace
        tvCountItems.typeface = regularTypeFace
        tvAgenda.typeface = boldTypeFace
        tvTitleTop.typeface = regularTypeFace
        tvLearnMore.typeface = boldTypeFace
        tvOnTopic.typeface = semiBoldTypeFace
        tvMeetChat.typeface = semiBoldTypeFace
        tvBanForDay.typeface = boldTypeFace
        tvYouBan.typeface = regularTypeFace
        tvBanReason.typeface = regularTypeFace
    }

    override fun animateMeet() {
//        flMeet.buildDrawingCache()
//        val duplicateView = ImageView(context)
//        duplicateView.setImageResource(R.drawable.ic_flame_meeting)
//        root.addView(duplicateView)
//
//        duplicateView.x = tvMeet.x
//        duplicateView.y = root.y * 0.9f
//
//        duplicateView.animate()
//                .x(flMeets.x)
//                .y(flMeets.y)
//                .alpha(0f)
//                .start()
    }

    override fun createPresenter() = inject<ChatPresenter>().value
    override fun provideLayoutId() = R.layout.fragment_chat

    private fun setKeyboardListenerEvent() {
        val activityRoot = (activity!!.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        val rect = Rect()

        keyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
            activityRoot.getWindowVisibleDisplayFrame(rect)

            if (!initOldHeight && rect.height() > 0) {
                initOldHeight = true
                oldHeight = activityRoot.rootView.height - rect.height()
            }

            keyboardHeight = activityRoot.rootView.height - rect.height() - oldHeight
            val value = keyboardHeight > oldHeight && !keyboardVisible

            if (keyboardHeight > oldHeight && !keyboardVisible) {
                showMessageBar()
                keyboardVisible = true
                (activity as? MainActivity)?.hideNavigationBottom()
            } else if (keyboardHeight < oldHeight && keyboardVisible) {
                keyboardVisible = false
                showFab()
                (activity as? MainActivity)?.showNavigationBottom()
            }
        }

        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(keyboardListener)
    }

    private fun clearKeyboardListener() {
        if (keyboardListener != null) {
            (activity!!.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0).viewTreeObserver.removeOnGlobalLayoutListener(keyboardListener)
        }
    }
}
