package com.kard.test.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kard.test.app.R
import com.kard.test.app.data.Transaction
import com.kard.test.app.data.TransactionOwnerType
import com.kard.test.app.domain.KardNetworkClient
import com.kard.test.app.ui.adapter.TransactionsAdapter

class TransactionsFragment: Fragment() {

    companion object {
        private const val EXTRA_TRANSACTION_OWNER_TYPE = "extra_transaction_owner_type"

        // Ctor
        fun newInstance(type: TransactionOwnerType): TransactionsFragment {
            return TransactionsFragment().apply {
                val bundle = Bundle()
                bundle.putInt(EXTRA_TRANSACTION_OWNER_TYPE, type.ordinal)
                this.arguments = bundle
            }
        }
    }

    // Members
    private val transactions = ArrayList<Transaction>()
    private lateinit var type: TransactionOwnerType
    // Dependencies injection (Usually injected with dagger2)
    private lateinit var adapter: TransactionsAdapter

    // Views
    private lateinit var toolbar: Toolbar
    private lateinit var list: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    // UI lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = TransactionOwnerType.fromBundle(arguments, EXTRA_TRANSACTION_OWNER_TYPE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        toolbar = view.findViewById(R.id.toolbar)
        list = view.findViewById(R.id.list)
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        progressBar = view.findViewById(R.id.progress_bar)

        setupToolbar()
        setupList()
    }

    override fun onResume() {
        super.onResume()
        loadData()

        KardNetworkClient(context!!).getTransactions()
    }

    // Private helper

    private fun loadData() {
        if (transactions.isEmpty()) {
            progressBar.visibility = View.VISIBLE
        }

        // swipeToRefresh.isRefreshing = false
    }

    private fun setupToolbar() {
        toolbar.title = getString(when (type) {
            TransactionOwnerType.ME -> R.string.transaction_owner_type_me
            TransactionOwnerType.FRIENDS -> R.string.transaction_owner_type_Friends
        })
        toolbar.setTitleTextColor(resources.getColor(R.color.colorAccent))
    }

    private fun setupList() {
        // Replace with DI
        adapter = TransactionsAdapter(context)
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.adapter = adapter

        // Swipe to refresh
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = true
            loadData()
        }
    }

}