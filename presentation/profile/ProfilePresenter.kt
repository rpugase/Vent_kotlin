package org.zapomni.venturers.presentation.profile

import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.CardType
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BasePresenter

class ProfilePresenter(private val userInteractor: UserInteractor) : BasePresenter<ProfileView>() {

    private var user: User? = null

    override fun attachView(view: ProfileView) {
        super.attachView(view)
        userInteractor.getUser().execute({ user = it; initUser() })
    }

    fun issueWithdrawBonus(price: Int) {
        userInteractor.issueWithdrawBonus(price)
                .execute({ view?.setBonusResult(it, price) })
    }

    private fun initUser() {
        view?.setUser(user)

        if (user?.bonusCard != null) {
            when (user!!.bonusCard.cardType) {
                CardType.NONE -> view?.setBonusNone(user!!.bonusCard)
                CardType.GOLD -> view?.setBonusGold(user!!.bonusCard)
                CardType.GREEN -> view?.setBonusGreen(user!!.bonusCard)
                CardType.BLUE -> view?.setBonusBlue(user!!.bonusCard)
            }
        }

        if (user?.events != null) {
            view?.setEventList(user!!.events!!)
        }
    }
}