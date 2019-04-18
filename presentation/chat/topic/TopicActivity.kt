package org.zapomni.venturers.presentation.chat.topic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.startActivityForResult
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.extensions.replace
import org.zapomni.venturers.presentation.chat.topic.main.TopicFragment

class TopicActivity : AppCompatActivity() {

    companion object {
        const val REQ_TOPIC = 142
        const val EXTRA_AGENDA = "EXTRA_AGENDA"
        const val EXTRA_POLL = "EXTRA_POLL"

        fun start(activity: Activity?) {
            activity?.startActivityForResult<TopicActivity>(REQ_TOPIC)
        }
    }

    private val topicFragment = TopicFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)
//        window?.statusBarColor = Color.parseColor("#F0EFEB")

        topicFragment.replace(supportFragmentManager)
    }

    fun sendUriToTopicFragment(uri: Uri?) {
        topicFragment.showBackgroundUri(uri)
    }

    fun sendBackgroundToTopicFragment(backgroundModel: BackgroundModel) {
        topicFragment.showBackgroundModel(backgroundModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        topicFragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        window?.statusBarColor = Color.parseColor("#F0EFEB")
    }
}