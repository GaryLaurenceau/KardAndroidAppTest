package com.kard.test.app.domain

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.kard.test.app.MeQuery
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient


class KardNetworkClient {

    // Const
    private val KARD_NETWORK_API = "https://staging.kard.eu/graphql"

    private val apolloClient: ApolloClient

    class MyInterceptor: Interceptor {
        private val OAUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiZWQ4ZjgwMy0yMDNmLTQ2ODUtYjcxNi03YWJmZjc2NDM5NDMiLCJzdWIiOiJkMTNjYTYxNC1hOTJjLTRhNjQtOWQ3NS1jNzg4ZjQ5OGRiZTEiLCJzY3AiOiJ1c2VyIiwiYXVkIjpudWxsLCJpYXQiOjE1NjIyNzUyNDMsImV4cCI6MTU2Mjg4MDA0M30.JUto3mmX7DvU-WDVIR1ZXoeQkoZ0rE8WTRGLztmaRF0"
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", OAUTH_TOKEN)
                .build()
            return chain.proceed(newRequest)
        }
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(MyInterceptor())
//            .addInterceptor { chain: Interceptor.Chain ->
//                    val newRequest = chain.request().newBuilder()
//                        .addHeader("Authorization", OAUTH_TOKEN)
//                        .build()
//                    return@addInterceptor chain.proceed(newRequest)
//            }
            .build()

        apolloClient = ApolloClient.builder()
            .serverUrl(KARD_NETWORK_API)
            .okHttpClient(okHttpClient)
            .build()
    }

    fun getTransactions(): Observable<Response<MeQuery.Data>> {
        return Rx2Apollo.from(apolloClient.query(MeQuery.builder().build()))
    }
}