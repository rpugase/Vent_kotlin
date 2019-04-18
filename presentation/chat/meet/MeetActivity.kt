package org.zapomni.venturers.presentation.chat.meet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.chat.meet.list.MeetsFragment
import org.zapomni.venturers.presentation.chat.meet.newm.NewMeetFragment

class MeetActivity : AppCompatActivity() {

    companion object {
        const val REQ_NEW_MEET = 220
        const val EXTRA_MEET = "EXTRA_MEET"

        const val ACTION_NEW_MEET = "NEW_MEET"
        const val ACTION_MEETS = "MEETS"
        const val EXTRA_MEETS_LIST = "EXTRA_MEETS_LIST"

        fun startNewMeet(activity: Activity?) {
            activity?.startActivityForResult(Intent(activity, MeetActivity::class.java).setAction(ACTION_NEW_MEET), REQ_NEW_MEET)
        }

        fun startMeets(activity: Activity?, meets: Array<Message>) {
            activity?.startActivity(Intent(activity, MeetActivity::class.java)
                    .setAction(ACTION_MEETS)
                    .putExtra(EXTRA_MEETS_LIST, meets))
        }
    }

    val TAG_FRAGMENT = MeetActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_topic)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                        when (intent.action) {
                            ACTION_NEW_MEET -> NewMeetFragment()
                            ACTION_MEETS -> MeetsFragment.newInstance(intent.getParcelableArrayExtra(EXTRA_MEETS_LIST))
                            else -> throw IllegalArgumentException("No such action")
                        },
                        TAG_FRAGMENT)
                .commit()
    }
}