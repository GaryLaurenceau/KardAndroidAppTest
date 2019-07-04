package com.kard.test.app.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kard.test.app.data.Transaction

class TransactionViewHolder(view: View): RecyclerView.ViewHolder(view) {

    // Views
    private lateinit var icon: ImageView
    private lateinit var label: TextView
    private lateinit var details: TextView
    private lateinit var amount: TextView

    fun show(transaction: Transaction) {

    }

}