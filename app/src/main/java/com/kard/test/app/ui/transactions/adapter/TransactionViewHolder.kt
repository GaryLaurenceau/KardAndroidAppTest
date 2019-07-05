package com.kard.test.app.ui.transactions.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kard.test.app.MeQuery
import com.kard.test.app.R
import com.kard.test.app.extensions.toText
import com.kard.test.app.ui.transactions.listener.TransactionClickListener

class TransactionViewHolder(
    view: View,
    private val transactionClickListener: TransactionClickListener
): RecyclerView.ViewHolder(view) {

    // Views
    private val icon: ImageView = itemView.findViewById(R.id.icon)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val category: TextView = itemView.findViewById(R.id.category)
    private val amount: TextView = itemView.findViewById(R.id.amount)

    fun show(transaction: MeQuery.Edge) {
        // Icon
        Glide.with(itemView.context)
            .load(transaction.node()?.category()?.image()?.url())
            .into(icon)

        // Title & category
        title.text = transaction.node()?.title()
        category.text = transaction.node()?.category()?.name()

        // Amount
        amount.text = transaction.node()?.amount()?.toText()

        // Click listener
        itemView.setOnClickListener {
            transactionClickListener.onTransactionClick(transaction)
        }
    }
}