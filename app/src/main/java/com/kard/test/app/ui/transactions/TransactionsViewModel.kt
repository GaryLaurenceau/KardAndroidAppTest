package com.kard.test.app.ui.transactions

import androidx.lifecycle.ViewModel
import com.kard.test.app.MeQuery

class TransactionsViewModel: ViewModel() {

    var transactions: ArrayList<MeQuery.Edge> = ArrayList()

}