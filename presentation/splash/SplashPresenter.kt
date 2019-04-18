package org.zapomni.venturers.presentation.splash

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.zapomni.venturers.domain.TripInteractor
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.navigation.AuthNavigation
import org.zapomni.venturers.domain.model.navigation.ModuleNavigation
import org.zapomni.venturers.presentation.base.BasePresenter

class SplashPresenter(private val userInteractor: UserInteractor,
                      private val tripInteractor: TripInteractor) : BasePresenter<SplashView>() {

    private var moduleNavigation: ModuleNavigation? = null

    override fun attachView(view: SplashView) {
        super.attachView(view)

        Single.zip(userInteractor.getUser(true), tripInteractor.getTrip(),
                BiFunction<User, Trip, Home> { user, trip -> Home(user, trip) })
                .execute({
                    if (it.user.phoneNumber == null) {
                        moduleNavigation = ModuleNavigation.PROFILE
                        userInteractor.authNavigation = AuthNavigation.PHONE
                    } else if (it.user.name == null || it.user.surname == null) {
                        moduleNavigation = ModuleNavigation.PROFILE
                        userInteractor.authNavigation = AuthNavigation.PROFILE
                    } else {
                        moduleNavigation = if (it.trip.type == Trip.Type.TRIP_THIRD)
                            ModuleNavigation.TRIP else ModuleNavigation.MAIN
                    }
                }, {
                    moduleNavigation = ModuleNavigation.PROFILE
                    userInteractor.authNavigation = AuthNavigation.PHONE
                }, withLoading = false)
    }

    fun goNext() {
        when (moduleNavigation) {
            ModuleNavigation.PROFILE -> {
                view?.stopAnimationRoo()
                view?.showRegistrationView()
            }
            ModuleNavigation.MAIN -> {
                view?.stopAnimationRoo()
                view?.showMainView()
            }
            ModuleNavigation.TRIP -> {
                view?.stopAnimationRoo()
                view?.showTripView()
            }
        }
    }

    private inner class Home(val user: User,
                             val trip: Trip)
}