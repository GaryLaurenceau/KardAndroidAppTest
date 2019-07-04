package com.kard.test.app

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.kard.test.app.data.TransactionOwnerType
import com.kard.test.app.ui.TransactionsFragment

class HomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Update view
        if (savedInstanceState == null) {
            displayMeTab()
        }
    }

    private fun displayMeTab() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, TransactionsFragment.newInstance(TransactionOwnerType.ME))
    }

}