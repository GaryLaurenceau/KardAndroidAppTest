package com.kard.test.app.domain.response

import com.kard.test.app.data.Transaction

class KardTransaction(
    val data: KardTransactionData? = null
)

class KardTransactionData(
    val me: KardTransactionMe
)

class KardTransactionMe(
    val email: String,
    val transactions: Transactions
)

class Transactions(
    val edges: List<Transaction>
)