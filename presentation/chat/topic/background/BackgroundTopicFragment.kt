package org.zapomni.venturers.presentation.chat.topic.background

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_background_topic.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.extensions.pickImageFromGallery
import org.zapomni.venturers.presentation.adapter.BackgroundAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.chat.topic.TopicActivity

class BackgroundTopicFragment : BaseFragment<BackgroundTopicView, BackgroundTopicPresenter>(),
        BackgroundTopicView {

    companion object {
        fun newInstance() = BackgroundTopicFragment().apply {
            arguments = Bundle()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.window?.statusBarColor = Color.WHITE
        hideKeyBoard()

        btnBack.setOnClickListener { activity?.onBackPressed() }
    }

    override fun loadBackgrounds(backgrounds: List<BackgroundModel>) {
        rvBackground.adapter = BackgroundAdapter(presenter).apply { setItems(backgrounds) }
    }

    override fun openGallery() {
        activity?.pickImageFromGallery {
            (activity as TopicActivity).sendUriToTopicFragment(it)
            activity?.onBackPressed()
        }
    }

    override fun sendBackgroundModel(backgroundModel: BackgroundModel) {
        (activity as TopicActivity).sendBackgroundToTopicFragment(backgroundModel)
        activity?.onBackPressed()
    }

    override fun setTypeFace() {
        tvTakeBackground.typeface = boldTypeFace
        tvOrLoadYourself.typeface = regularTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_background_topic
    override fun createPresenter() = inject<BackgroundTopicPresenter>().value
}