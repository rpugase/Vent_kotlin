package org.zapomni.venturers.presentation.curators

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import kotlinx.android.synthetic.main.fragment_curators.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.presentation.adapter.CuratorAdapter
import org.zapomni.venturers.presentation.base.BaseFragment

class CuratorsFragment : BaseFragment<CuratorsView, CuratorsPresenter>(), CuratorsView {

    companion object {
        fun newInstance(): CuratorsFragment {
            return CuratorsFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { activity?.onBackPressed() }
    }

    override fun provideLayoutId() = R.layout.fragment_curators

    override fun createPresenter() = inject<CuratorsPresenter>().value

    override fun loadConductors(curators: List<Curator>) {
        rvCurators.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        rvCurators.adapter = CuratorAdapter(fragmentManager!!).apply { setItems(curators) }
    }

    override fun setTypeFace() {
        tvCuratorsRating.typeface = boldTypeFace
    }
}