package org.zapomni.venturers.presentation.trip

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.android.synthetic.main.fragment_trip_action.view.*
import kotlinx.android.synthetic.main.layout_trip_mini.*
import kotlinx.android.synthetic.main.layout_trip_step_1.*
import kotlinx.android.synthetic.main.layout_trip_step_2.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.adapter.TripActionAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.dialog.PointCollectionDialog
import org.zapomni.venturers.presentation.main.MainActivity

class TripFragment : BaseFragment<TripView, TripPresenter>(), TripView {

    private val adapter = TripActionAdapter()
    private val eventTouchListener: (View, MotionEvent) -> Boolean = { view, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            (view as CardView).setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            (view as CardView).setCardBackgroundColor(Color.WHITE)
            view.performClick()
        }

        true
    }

    private var overlayViewAction: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        llNoTrip.visibility = View.GONE
        llFirstStage.visibility = View.GONE
        llSecondStage.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showNoStage() {
        llNoTrip.visibility = View.VISIBLE
        llFirstStage.visibility = View.GONE
        llSecondStage.visibility = View.GONE
        llMiniStage.visibility = View.GONE
    }

    override fun showFirstStage(trip: Trip) {
        llNoTrip.visibility = View.GONE
        llFirstStage.visibility = View.VISIBLE
        llSecondStage.visibility = View.GONE
        llMiniStage.visibility = View.GONE

        imgEventHead.loadImage(trip.event.headImages.firstOrNull())
        tvEventTitle.text = trip.event.title
        flipClock.start(trip.event.date.startDate.time)
        llWhatTakes.setOnClickListener { TripActivity.startWhatTakes(activity, trip.whatTake, trip.event.headImages.firstOrNull()) }
        llCollectionPoint.setOnClickListener { PointCollectionDialog.show(fragmentManager, trip.startPoint) }
        llEventsChat.setOnClickListener { presenter.openEventsChat() }

        llEventsChat.setOnTouchListener(eventTouchListener)
        llWhatTakes.setOnTouchListener(eventTouchListener)
        llCollectionPoint.setOnTouchListener(eventTouchListener)

        with(trip.event.curator!!) {
            imgAvatar.loadImage(image)
            tvNameCurator.text = "$name $surname"
            tvPhoneCurator.text = phone?.toPrettyPhoneNumber()
            tvAboutCurator.text = about

            llCurator.setOnClickListener { org.zapomni.venturers.presentation.dialog.CuratorDialog.show(fragmentManager, this, true) }
        }
    }

    override fun showSecondStage(trip: Trip) {
        llNoTrip.visibility = View.GONE
        llFirstStage.visibility = View.GONE
        llSecondStage.visibility = View.VISIBLE
        llMiniStage.visibility = View.GONE

        tvTitle.text = trip.event.title
        rvActions.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        rvActions.adapter = adapter
        adapter.setItems(trip.tripActions)

        adapter.onClickListener = {
            overlayViewAction = layoutInflater.inflate(R.layout.fragment_trip_action, null, false)
            val dialog = ImageViewer.Builder(context, trip.tripActions[it].images)
                    .setOverlayView(overlayViewAction)
                    .hideStatusBar(true)
                    .setImageChangeListener { page ->
                        if (trip.tripActions.isNotEmpty()) {
                            setActionScreen(trip.tripActions[it].descriptions[page],
                                    trip.tripActions[it].images.size, page)
                        }
                    }
                    .build()

            overlayViewAction?.findViewById<ImageButton>(R.id.btnCancel)?.setOnClickListener { dialog.onDismiss() }

            dialog.show()
        }

        with(trip.event.curator!!) {
            imgAvatar2.loadImage(image)
            tvNameCurator2.text = "$name $surname"
            tvPhoneCurator2.text = phone?.toPrettyPhoneNumber()

            tvGoChat.setOnClickListener { presenter.openEventsChat() }
            llCurator2.setOnClickListener { org.zapomni.venturers.presentation.dialog.CuratorDialog.show(fragmentManager, this, true) }
        }
    }

    override fun goToChat() {
        (activity as MainActivity).goChat()
    }

    override fun showMiniTrip(trip: Trip) {
        llFirstStage.visibility = View.GONE
        llNoTrip.visibility = View.GONE
        llSecondStage.visibility = View.GONE
        llMiniStage.visibility = View.VISIBLE

        imgEventHeadMini.loadImage(trip.event.headImages.firstOrNull())
        tvEventTitleMini.text = trip.event.title
        flipClockMini.start(trip.event.date.startDate.time)
        llCollectionPointMini.setOnClickListener { PointCollectionDialog.show(fragmentManager, trip.startPoint) }

        llCollectionPointMini.setOnTouchListener(eventTouchListener)

        with(trip.event.curator!!) {
            imgAvatarMini.loadImage(image)
            tvNameCuratorMini.text = "$name $surname"
            tvPhoneCuratorMini.text = phone?.toPrettyPhoneNumber()
            tvAboutCuratorMini.text = about

            llCuratorMini.setOnClickListener { org.zapomni.venturers.presentation.dialog.CuratorDialog.show(fragmentManager, this, true) }
        }
    }

    override fun setTypeFace() {
        tvGoToTrip.typeface = mediumTypeFace
        tvAboutGoTrip.typeface = regularTypeFace

        tvEventTitle.typeface = boldTypeFace
        tvBeforeStart.typeface = boldTypeFace
        tvYourCurator.typeface = boldTypeFace
        tvYourCurator2.typeface = boldTypeFace
        tvNameCurator.typeface = mediumTypeFace
        tvNameCurator2.typeface = mediumTypeFace
        tvPhoneCurator.typeface = regularTypeFace
        tvPhoneCurator2.typeface = regularTypeFace
        tvAboutCurator.typeface = regularTypeFace
        tvEventsChat.typeface = mediumTypeFace
        tvWhatTakes.typeface = mediumTypeFace
        tvCollectionPoint.typeface = mediumTypeFace
        tvTitle.typeface = boldTypeFace
        tvYourAreOnTheWay.typeface = boldTypeFace
        tvGoChat.typeface = regularTypeFace

        tvEventTitleMini.typeface = boldTypeFace
        tvBeforeStartMini.typeface = boldTypeFace
        tvYourCuratorMini.typeface = boldTypeFace
        tvNameCuratorMini.typeface = mediumTypeFace
        tvPhoneCuratorMini.typeface = regularTypeFace
        tvAboutCuratorMini.typeface = regularTypeFace
        tvCollectionPointMini.typeface = mediumTypeFace
    }

    private fun setActionScreen(description: String, imagesCount: Int, pickedImage: Int) {
        with(overlayViewAction!!) {
            tvInfoAboutTrip.typeface = boldTypeFace
            tvInfo.typeface = regularTypeFace

            llContent.visibility = if (description.isEmpty()) View.GONE else View.VISIBLE

            tvInfo.text = description

            if (pickedImage == 0) {
                rgPoints.removeAllViews()

                (0 until imagesCount).forEach {
                    rgPoints.addView(layoutInflater.inflate(R.layout.rb_action, null, false)
                            .apply { id = it })
                }
            }

            rgPoints.check(pickedImage)
        }
    }

    override fun provideLayoutId() = R.layout.fragment_trip
    override fun createPresenter() = inject<TripPresenter>().value
}