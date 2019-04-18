package org.zapomni.venturers.presentation.event

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.text.Html
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.makeCall
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.adapter.DayMeetAdapter
import org.zapomni.venturers.presentation.adapter.EventContentAdapter
import org.zapomni.venturers.presentation.adapter.EventsPriceAdapter
import org.zapomni.venturers.presentation.adapter.PhotoAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.holder.EventContentHolder
import java.text.SimpleDateFormat
import java.util.*

class EventFragment : BaseFragment<EventView, EventPresenter>(), EventView {

    companion object {
        fun newInstance() = EventFragment().apply { arguments = Bundle.EMPTY }
    }

    private val photoAdapter = PhotoAdapter().apply { onImageClickListener = { presenter.showPhoto(it) } }
    private val contentAdapter = EventContentAdapter()
    private val priceAdapter = EventsPriceAdapter()
    private val daysAdapter = DayMeetAdapter()
    private val daysLayoutManager by lazy { FlexboxLayoutManager(context).apply { justifyContent = JustifyContent.CENTER } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { activity?.onBackPressed() }
        rvDays.layoutManager = daysLayoutManager
        rvDays.adapter = daysAdapter

        (rvContent.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rvContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvContent.adapter = contentAdapter

        rvPhotos.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvPhotos.adapter = photoAdapter

        btnBookIt.setOnClickListener { context?.toast("Неа") }
    }

    override fun setEvent(event: Event) {
        tvTitle.text = event.title
        tvInfo.text = event.info
        tvStartDate.text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(event.date.startDate)
        tvStartTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.date.startDate)
        tvFinishDate.text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(event.date.endDate)
        tvFinishTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.date.endDate)
        tvPriceDisclaimer.text = Html.fromHtml(event.priceDisclaimer)
        rvEventsPrice.adapter = priceAdapter
        priceAdapter.addItems(event.date.offers)

        val contentHead = event.content.map { EventContentHolder.EventContent(it.first) }.toMutableList()
        if (event.photos.isNotEmpty()) {
            contentHead.add(EventContentHolder.EventContent(context!!.getString(R.string.photo), showPoint = false))
        } else {
            contentHead.lastOrNull()?.showPoint = false
        }
        photoAdapter.setItems(event.photos)
        selectContent(event, 0)
        contentAdapter.onContentClickListener = { selectContent(event, it) }
        contentAdapter.setItems(contentHead)

        tvPhoneNumber.text = event.curator?.phone?.toPrettyPhoneNumber()
        tvPhoneNumber.setOnClickListener { if (event.curator?.phone != null) activity?.makeCall(event.curator.phone) }
    }

    override fun showDays(days: List<String>) {
        if (days.isNotEmpty()) {
            tvDaysOfEvents.visibility = View.VISIBLE
            rvDays.visibility = View.VISIBLE
            daysAdapter.setItems(days)
        }
    }

    override fun showPhotos(photos: List<String>, startPosition: Int) {
        ImageViewer.Builder(context, photos)
                .setStartPosition(startPosition)
                .show()
    }

    override fun showHeadImage(imgUrl: String, needToAnimate: Boolean) {
        imgHead.setImageURI(imgUrl)
    }

    override fun prefetchImages(images: List<String>) {
        images.forEach { Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(it), context) }
    }

    override fun setTypeFace() {
        tvTitle.typeface = boldTypeFace
        tvInfo.typeface = regularTypeFace

        tvStart.typeface = regularTypeFace
        tvStartDate.typeface = boldTypeFace
        tvStartTime.typeface = regularTypeFace
        tvDaysOfEvents.typeface = mediumTypeFace
        tvFinish.typeface = regularTypeFace
        tvFinishDate.typeface = boldTypeFace
        tvFinishTime.typeface = regularTypeFace

        tvTourPrice.typeface = boldTypeFace
        tvPriceDisclaimer.typeface = regularTypeFace
        btnBookIt.typeface = boldTypeFace

        tvYouCanCall.typeface = regularTypeFace
        tvPhoneNumber.typeface = boldTypeFace
    }

    private fun selectContent(event: Event, position: Int) {
        if (position == contentAdapter.items.size - 1 && event.photos.isNotEmpty()) {
            tvContent.visibility = View.GONE
            rvPhotos.visibility = View.VISIBLE
        } else {
            tvContent.visibility = View.VISIBLE
            rvPhotos.visibility = View.GONE
            tvContent.text = Html.fromHtml(event.content[position].second)
        }
    }

    override fun provideLayoutId() = R.layout.fragment_event
    override fun createPresenter() = inject<EventPresenter>().value
}