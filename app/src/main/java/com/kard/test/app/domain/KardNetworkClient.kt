package com.kard.test.app.domain

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.kard.test.app.domain.response.KardTransaction
import io.reactivex.Observable
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kard.test.app.MeQuery
import okhttp3.OkHttpClient


class KardNetworkClient(private val context: Context) {

    // Const
    private val KARD_NETWORK_API = "https://staging.kard.eu/"

    private val apolloClient: ApolloClient

    init {
        val okHttpClient = OkHttpClient.Builder().build()

        apolloClient = ApolloClient.builder()
            .serverUrl(KARD_NETWORK_API)
            .okHttpClient(okHttpClient)
            .build()
    }

    fun getTransactions(): KardTransaction {
        apolloClient.query(MeQuery.builder().build())
            .enqueue(object: ApolloCall.Callback<MeQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Log.e("PLOP", "On failure: ${e.localizedMessage}")
                    e.printStackTrace()
                }

                override fun onResponse(response: Response<MeQuery.Data>) {
                    Log.d("PLOP", "On success")
                }
            })
        return KardTransaction()
    }
}