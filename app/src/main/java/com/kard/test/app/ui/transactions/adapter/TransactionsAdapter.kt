package com.kard.test.app.ui.transactions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kard.test.app.MeQuery
import com.kard.test.app.R
import com.kard.test.app.ui.transactions.listener.TransactionClickListener

class TransactionsAdapter(
    context: Context?,
    private val transactionClickListener: TransactionClickListener
): RecyclerView.Adapter<TransactionViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var objects:ArrayList<MeQuery.Edge> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            inflater.inflate(
                R.layout.item_transation,
                parent,
                false
            ), transactionClickListener
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.show(objects[position])
    }

    override fun getItemCount(): Int {
        return objects.size
    }

}