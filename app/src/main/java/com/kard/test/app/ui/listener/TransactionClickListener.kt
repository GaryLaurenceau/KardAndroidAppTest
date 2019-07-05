package com.kard.test.app.ui.listener

import com.kard.test.app.MeQuery

interface TransactionClickListener {

    fun onTransactionClick(transaction: MeQuery.Edge)

}