package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.BonusCard
import org.zapomni.venturers.extensions.watchText
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class BonusDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_PRICE = "ARG_PRICE" // Int

        fun newInstance(fragmentManager: FragmentManager?, bonusCard: BonusCard): BonusDialog {
            return BonusDialog().apply {
                arguments = Bundle().apply { putInt(ARG_PRICE, bonusCard.cash) }
                show(fragmentManager, BonusDialog::class.java.name)
            }
        }
    }

    var onWithdrawClick: ((bonusCash: Int) -> Unit)? = null
    var onHistoryClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_bonus, null)

        val price = arguments?.getInt(ARG_PRICE)

        with(mView) {
            val etSum = findViewById<EditText>(R.id.etEnterSum)

            findViewById<FloatingActionButton>(R.id.btnFinish).setOnClickListener { dismiss() }
            findViewById<Button>(R.id.btnUnderstand).setOnClickListener { dismiss() }
            findViewById<TextView>(R.id.tvPromoHistory).setOnClickListener { onHistoryClick?.invoke() }
            findViewById<Button>(R.id.btnWithdraw).setOnClickListener {
                val priceString = etSum.text.toString()
                if (priceString.isNotEmpty()) {
                    val bonusCash = priceString.toInt()
                    if (bonusCash == 0 || bonusCash <= price ?: 0) {
                        onWithdrawClick?.invoke(bonusCash)
                    }
                }
            }

            etSum.watchText {
                if (it.isNotEmpty() && it.toInt() > price ?: 0) {
                    etSum.setText(price.toString())
                    etSum.setSelection(price.toString().length)
                }
            }

            if (price != null) {
                findViewById<TextView>(R.id.tvBonus).text = getString(R.string.cash_uah, price)
                etSum.hint = getString(R.string.cash_uah, arguments!!.getInt(ARG_PRICE))
            }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    fun onWithdrawResult(promo: String, price: Int) {
        with(mView) {
            findViewById<LinearLayout>(R.id.llYourCash).visibility = View.GONE
            findViewById<LinearLayout>(R.id.llEnterPrice).visibility = View.GONE
            findViewById<LinearLayout>(R.id.llYourPromo).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.llPromoMessage).visibility = View.VISIBLE

            findViewById<TextView>(R.id.tvPromo).text = promo
            findViewById<TextView>(R.id.tvYourWithdraw).text = getString(R.string.you_withdraw, price)
        }
    }

    override fun setTypeFace() {

    }
}