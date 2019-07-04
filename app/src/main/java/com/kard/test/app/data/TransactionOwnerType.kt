package com.kard.test.app.data

import android.os.Bundle

enum class TransactionOwnerType {

    ME,
    FRIENDS;

    companion object {

        private fun default(): TransactionOwnerType {
            return ME
        }

        fun fromBundle(bundle: Bundle?, key: String): TransactionOwnerType {
            return values().firstOrNull() { it.ordinal == bundle?.getInt(key, default().ordinal) } ?: default()
        }
    }

}