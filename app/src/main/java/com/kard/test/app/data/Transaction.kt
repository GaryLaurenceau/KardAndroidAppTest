package com.kard.test.app.data

data class Transaction(
    val cursor: String,
    val node: Node
) {

    data class Node(
        val id: String,
        val title: String,
        val description: String,
//        val user: User,
        val amount: Amount
    )

    data class Amount(
        val currency: Currency,
        val value: Double
    )

    data class Currency(
        val name: String,
        val symbol: String
    )

}