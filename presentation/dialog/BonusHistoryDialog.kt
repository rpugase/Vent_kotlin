package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Bonus
import org.zapomni.venturers.domain.model.BonusCard
import org.zapomni.venturers.presentation.adapter.BonusAdapter
import org.zapomni.venturers.presentation.base.BaseDialogFragment


class BonusHistoryDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_BONUSES = "ARG_BONUSES" // parcelable array

        fun newInstance(fragmentManager: FragmentManager?, bonusCard: BonusCard): BonusHistoryDialog {
            return BonusHistoryDialog().apply {
                arguments = Bundle().apply { putParcelableArray(ARG_BONUSES, bonusCard.bonuses.toTypedArray()) }
                show(fragmentManager, BonusDialog::class.java.name)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_bonus_history, null)

        with(mView) {
            findViewById<FloatingActionButton>(R.id.btnFinish).setOnClickListener { dismiss() }

            findViewById<RecyclerView>(R.id.rvBonuses)
                    .apply {
                        addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
                        adapter = BonusAdapter().apply {
                            setItems(arguments?.getParcelableArray(ARG_BONUSES)?.map { it as Bonus })
                        }
                    }

        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {

    }
}