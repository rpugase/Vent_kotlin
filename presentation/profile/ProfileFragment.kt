package org.zapomni.venturers.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.BonusCard
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.adapter.EventProfileAdapter
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.dialog.BonusDialog
import org.zapomni.venturers.presentation.dialog.BonusHistoryDialog
import org.zapomni.venturers.presentation.dialog.HowWorkDialog
import org.zapomni.venturers.presentation.profile.edit.ProfileEditFragment

class ProfileFragment : BaseFragment<ProfileView, ProfilePresenter>(), ProfileView {
    companion object {

        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle()
        }
    }
    private var bonusDialog: BonusDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnHowWork.setOnClickListener { openHowItWork() }
        btnEdit.setOnClickListener { editProfile() }
    }

    override fun setTypeFace() {
        tvName.typeface = boldTypeFace
        tvPhone.typeface = regularTypeFace
        tvInstagram.typeface = regularTypeFace
        tvCardType.typeface = boldTypeFace
        tvYourCash.typeface = semiBoldTypeFace
        tvCash.typeface = boldTypeFace
        btnMain.typeface = boldTypeFace
        tvFramesHall.typeface = boldTypeFace
    }

    override fun editProfile() {
        fragmentManager?.apply {
            beginTransaction()
                    .add(R.id.container, ProfileEditFragment.newInstance(), ProfileEditFragment::class.java.name)
                    .addToBackStack(ProfileEditFragment::class.java.name)
                    .commit()
        }
    }

    override fun provideLayoutId() = R.layout.fragment_profile

    override fun createPresenter() = inject<ProfilePresenter>().value

    @SuppressLint("SetTextI18n")
    override fun setUser(user: User?) {
        if (user != null) {
            tvName.text = "${user.name} ${user.surname}"
            tvPhone.text = user.phoneNumber?.toPrettyPhoneNumber()
            imgAvatar.loadImage(user.image)

            tvInstagram.visibility = if (user.instagram != null) View.VISIBLE else View.GONE
            tvInstagram.text = user.instagram
        }
    }

    override fun setEventList(list: List<Event>) {
        tvFramesHall.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE

        val adapter = EventProfileAdapter()
        rvEvents.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        rvEvents.adapter = adapter
        adapter.setItems(list)
    }

    override fun setBonusNone(bonusCard: BonusCard) {
        tvCardType.setText(R.string.no_card_type)
        btnMain.setText(R.string.how_promo_works)
        tvCash.text = getString(R.string.cash_uah, bonusCard.cash)
        btnMain.setOnClickListener { openHowItWork() }
        imgCardNo.visibility = View.VISIBLE
        imgCard.visibility = View.GONE
        btnHowWork.visibility = View.GONE
    }

    override fun setBonusGold(bonusCard: BonusCard) {
        tvCardType.setText(R.string.gold_card_type)
        btnMain.setText(R.string.get_promo)
        tvCash.text = getString(R.string.cash_uah, bonusCard.cash)
        btnMain.setOnClickListener { openBonusDialog(bonusCard) }
        imgCard.setImageResource(R.drawable.ic_card_gold)
    }

    override fun setBonusGreen(bonusCard: BonusCard) {
        tvCardType.setText(R.string.green_card_type)
        btnMain.setText(R.string.get_promo)
        tvCash.text = getString(R.string.cash_uah, bonusCard.cash)
        btnMain.setOnClickListener { openBonusDialog(bonusCard) }
        imgCard.setImageResource(R.drawable.ic_card_green)
    }

    override fun setBonusBlue(bonusCard: BonusCard) {
        tvCardType.setText(R.string.blue_card_type)
        btnMain.setText(R.string.get_promo)
        tvCash.text = getString(R.string.cash_uah, bonusCard.cash)
        btnMain.setOnClickListener { openBonusDialog(bonusCard) }
        imgCard.setImageResource(R.drawable.ic_card_blue)
    }

    private fun openHowItWork() {
        HowWorkDialog().show(fragmentManager, HowWorkDialog::class.java.name)
    }

    private fun openBonusDialog(bonusCard: BonusCard) {
        bonusDialog = BonusDialog.newInstance(fragmentManager, bonusCard)
        bonusDialog?.onWithdrawClick = { presenter.issueWithdrawBonus(it) }
        bonusDialog?.onHistoryClick = { BonusHistoryDialog.newInstance(fragmentManager, bonusCard) }
    }

    override fun setBonusResult(code: String, price: Int) {
        bonusDialog?.onWithdrawResult(code, price)
    }
}