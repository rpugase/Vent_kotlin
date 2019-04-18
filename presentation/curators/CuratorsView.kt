package org.zapomni.venturers.presentation.curators

import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.presentation.base.BaseView

interface CuratorsView : BaseView {

    fun loadConductors(curators: List<Curator>)
}