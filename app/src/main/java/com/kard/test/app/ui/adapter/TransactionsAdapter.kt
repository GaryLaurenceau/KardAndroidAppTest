package com.kard.test.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kard.test.app.R
import com.kard.test.app.data.Transaction

class TransactionsAdapter(context: Context?): RecyclerView.Adapter<TransactionViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var objects:ArrayList<Transaction> = ArrayList()
    // var listener: IAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(inflater.inflate(R.layout.item_transation, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.show(objects[position])
    }

    override fun getItemCount(): Int {
        return objects.size
    }

}