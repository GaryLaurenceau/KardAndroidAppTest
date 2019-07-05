package com.kard.test.app.data

import android.content.Context
import android.os.Bundle
import com.kard.test.app.R

enum class TransactionOwnerType(private val displayNameRes: Int) {

    ME(R.string.transaction_owner_type_me),
    FRIENDS(R.string.transaction_owner_type_Friends);

    fun displayName(context: Context): String = context.getString(displayNameRes)

    companion object {

        private fun default(): TransactionOwnerType {
            return ME
        }

        fun fromBundle(bundle: Bundle?, key: String): TransactionOwnerType {
            return values().firstOrNull { it.ordinal == bundle?.getInt(key, default().ordinal) } ?: default()
        }
    }

}