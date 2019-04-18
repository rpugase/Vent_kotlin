package org.zapomni.venturers.presentation.profile

import org.zapomni.venturers.domain.model.BonusCard
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BaseView

interface ProfileView : BaseView {

    fun setUser(user: User?)
    fun setEventList(list: List<Event>)

    fun setBonusNone(bonusCard: BonusCard)
    fun setBonusGold(bonusCard: BonusCard)
    fun setBonusGreen(bonusCard: BonusCard)
    fun setBonusBlue(bonusCard: BonusCard)

    fun setBonusResult(code: String, price: Int)

    fun editProfile()
}