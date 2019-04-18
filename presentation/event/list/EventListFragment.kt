package org.zapomni.venturers.presentation.event.list

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import kotlinx.android.synthetic.main.fragment_event_list.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.makeCall
import org.zapomni.venturers.presentation.adapter.EventAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.event.EventFragment


class EventListFragment : BaseFragment<EventListView, EventListPresenter>(), EventListView {

    private val adapter = EventAdapter()

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = rvEvents.layoutManager.childCount
            val totalItemCount = rvEvents.layoutManager.itemCount
            val firstVisibleItemPosition = (rvEvents.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= 20) {
                presenter.loadMoreEvents()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (rvEvents.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rvEvents.adapter = adapter
        rvEvents.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                .apply { setDrawable(ContextCompat.getDrawable(context!!, R.drawable.offset_event)!!) })
        adapter.onItemClickListener = presenter::openEvent
        adapter.onPhoneClickListener = { activity?.makeCall(it) }
    }

    override fun onResume() {
        super.onResume()
        rvEvents.addOnScrollListener(recyclerViewOnScrollListener)
    }

    override fun onPause() {
        rvEvents.removeOnScrollListener(recyclerViewOnScrollListener)
        super.onPause()
    }

    override fun addEvents(events: List<Event>) {
        adapter.addItems(events)
    }

    override fun setImportantEvent(events: List<Event>) {
        adapter.setImportantEvents(events)
    }

    override fun showEvent() {
        activity?.apply {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, EventFragment.newInstance(), EventFragment::class.java.name)
                    .addToBackStack(EventFragment::class.java.name)
                    .commit()
        }
    }

    override fun provideLayoutId() = R.layout.fragment_event_list
    override fun createPresenter() = inject<EventListPresenter>().value
}