package org.zapomni.venturers.presentation.chat.meet.list

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import kotlinx.android.synthetic.main.fragment_meets.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.adapter.MeetAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import java.util.*

class MeetsFragment : BaseFragment<MeetsView, MeetsPresenter>(), MeetsView {

    companion object {
        private val ARG_MEETS = "ARG_MEETS"
        fun newInstance(meets: Array<Parcelable>): MeetsFragment {
            return MeetsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_MEETS, meets)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parcelableArray = arguments?.getParcelableArray(ARG_MEETS)
        val meets = Arrays.copyOf(parcelableArray, parcelableArray?.size
                ?: 0, Array<Message>::class.java)
        rvMeets.adapter = MeetAdapter(presenter).apply {
            onBackButtonClickListener = { activity?.onBackPressed() }
            setItems(meets.toList())
        }
    }

    override fun provideLayoutId() = R.layout.fragment_meets
    override fun createPresenter() = inject<MeetsPresenter>().value
}