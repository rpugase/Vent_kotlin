package org.zapomni.venturers.presentation.main

import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.presentation.base.BasePresenter

class MainPresenter(private val postInteractor: UserInteractor) : BasePresenter<MainView>()