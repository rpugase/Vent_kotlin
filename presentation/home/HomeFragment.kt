package org.zapomni.venturers.presentation.home

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_progress.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.*
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.getPhotoUrlById
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.base.SimpleAdapter
import org.zapomni.venturers.presentation.curators.CuratorsFragment
import org.zapomni.venturers.presentation.dialog.AdvertDialog
import org.zapomni.venturers.presentation.event.EventFragment
import org.zapomni.venturers.presentation.holder.TripHolder
import org.zapomni.venturers.presentation.holder.chat.HolderChatShort
import org.zapomni.venturers.presentation.main.MainActivity
import org.zapomni.venturers.presentation.profile.ProfileFragment
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment<HomeView, HomePresenter>(), HomeView {

    private val adapter = SimpleAdapter(HolderChatShort::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llTop.setOnClickListener { runProfile() }
        rvMessages.adapter = adapter

        imgStar.setOnClickListener {
            activity?.apply {
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, CuratorsFragment.newInstance(), CuratorsFragment::class.java.name)
                        .addToBackStack(CuratorsFragment::class.java.name)
                        .commit()
            }
        }
    }

    override fun runProfile() {
        activity?.apply {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, ProfileFragment.newInstance(), ProfileFragment::class.java.name)
                    .addToBackStack(ProfileFragment::class.java.name)
                    .commit()
        }
    }

    override fun setCard(cardType: CardType) {
        when (cardType) {
            CardType.GOLD -> imgCard.setImageResource(R.drawable.ic_card_gold)
            CardType.GREEN -> imgCard.setImageResource(R.drawable.ic_card_green)
            CardType.BLUE -> imgCard.setImageResource(R.drawable.ic_card_blue)
            CardType.NONE -> {
                imgCardNo.visibility = View.VISIBLE
                imgCard.visibility = View.GONE
            }
        }
    }

    override fun setChatWithEvent(messages: List<Message>, title: String?, imgHeader: String?, usersCount: Int) {
        llChatBlock.visibility = View.VISIBLE
        btnGoChat.visibility = View.VISIBLE
        progressChatBlock.visibility = View.GONE
        adapter.setItems(messages)
        tvUsersCount.text = usersCount.toString()
        if (title != null) {
            llEvent.visibility = View.VISIBLE
            tvDiscussNowAbout.text = title
            if (imgHeader != null) {
                imgEvent.loadImage(imgHeader.getPhotoUrlById())
            }
        }
        btnGoChat.setOnClickListener { (activity as? MainActivity)?.goChat() }
    }

    override fun setEvents(events: List<Event>) {
        tvNoMiss.visibility = View.VISIBLE
        llEventBlock.visibility = View.VISIBLE
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        llEventBlock.post {
            val imgHeight = (imgEvent1.width * 0.8).toInt()
            imgEvent1.layoutParams = imgEvent1.layoutParams.also { it.height = imgHeight }
            imgEvent2.layoutParams = imgEvent2.layoutParams.also { it.height = imgHeight }
            imgEvent3.layoutParams = imgEvent3.layoutParams.also { it.height = imgHeight }
        }
        events.forEachIndexed { index, event ->
            when (index) {
                0 -> {
                    imgEvent1.loadImage(event.headImages.first())
                    tvEventTitle1.text = event.title
                    tvEventDate1.text = dateFormat.format(event.date.startDate)
                    llEvent1.setOnClickListener { presenter.showEvent(events[0]) }
                }
                1 -> {
                    imgEvent2.loadImage(event.headImages.first())
                    tvEventTitle2.text = event.title
                    tvEventDate2.text = dateFormat.format(event.date.startDate)
                    llEvent2.setOnClickListener { presenter.showEvent(events[1]) }
                }
                2 -> {
                    imgEvent3.loadImage(event.headImages.first())
                    tvEventTitle3.text = event.title
                    tvEventDate3.text = dateFormat.format(event.date.startDate)
                    llEvent3.setOnClickListener { presenter.showEvent(events[2]) }
                }
            }
        }
    }

    override fun showEvent() {
        activity?.apply {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, EventFragment.newInstance(), EventFragment::class.java.name)
                    .addToBackStack(EventFragment::class.java.name)
                    .commit()
        }
    }

    override fun setProgress(max: Int, progress: Int) {
        this.progress.also {
            it.max = max
            it.progress = progress
        }
        tvProgress.text = getString(R.string.progress_text, progress, max)
    }

    override fun loadUser(user: User) {
        imgAvatar.loadImage(user.image)
    }

    override fun showTrips(trips: List<Trip>) {
        val adapter = SimpleAdapter(TripHolder::class.java)
        rvTrips.visibility = View.VISIBLE
        rvTrips.adapter = adapter
        adapter.setItems(trips)
    }

    override fun showAdvert(advert: Advert) {
        llAdvert.visibility = View.VISIBLE
        tvNoMiss.visibility = View.GONE
        llEventBlock.visibility = View.GONE
        Fresco.getImagePipeline().fetchDecodedImage(ImageRequestBuilder.newBuilderWithSource(Uri.parse(advert.headImage)).build(), context)
                .subscribe(object : BaseBitmapDataSubscriber() {
                    override fun onNewResultImpl(bitmap: Bitmap?) {
                        imgAdvertImage.setImageBitmap(bitmap)
                    }

                    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {}
                }, CallerThreadExecutor.getInstance())

        tvAdvertTitle.text = advert.text
        tvAdvertAbout.text = advert.text
        llAdvert.setOnClickListener { AdvertDialog.show(fragmentManager, advert) }
    }

    override fun setTypeFace() {
        tvProgress.typeface = mediumTypeFace
        tvMainChat.typeface = boldTypeFace
        tvUsersCount.typeface = regularTypeFace
        tvDiscussNow.typeface = boldTypeFace
        tvDiscussNowAbout.typeface = regularTypeFace
        btnGoChat.typeface = boldTypeFace
        tvNoMiss.typeface = boldTypeFace
        tvEventTitle1.typeface = boldTypeFace
        tvEventTitle2.typeface = boldTypeFace
        tvEventTitle3.typeface = boldTypeFace
        tvEventDate1.typeface = regularTypeFace
        tvEventDate2.typeface = regularTypeFace
        tvEventDate3.typeface = regularTypeFace
        tvAdvertTitle.typeface = boldTypeFace
        tvAdvertAbout.typeface = regularTypeFace
        tvAdvertMore.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_home
    override fun createPresenter() = inject<HomePresenter>().value
}