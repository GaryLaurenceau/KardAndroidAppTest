package com.kard.test.app.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kard.test.app.R
import com.kard.test.app.data.TransactionOwnerType
import com.kard.test.app.ui.transactions.TransactionsFragment

class HomeActivity: AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup View Pager
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = TransactionViewPager(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    class TransactionViewPager(
        private val context: Context,
        fragmentManager: FragmentManager
    ): FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TransactionsFragment.newInstance(TransactionOwnerType.FRIENDS)
                else -> TransactionsFragment.newInstance(TransactionOwnerType.ME)
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> TransactionOwnerType.FRIENDS.displayName(context)
                else -> TransactionOwnerType.ME.displayName(context)
            }
        }
    }
}