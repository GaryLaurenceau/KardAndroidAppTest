package com.kard.test.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kard.test.app.MeQuery
import com.kard.test.app.R
import com.kard.test.app.data.TransactionOwnerType
import com.kard.test.app.domain.KardNetworkClient
import com.kard.test.app.extensions.attachToPauseLifecycle
import com.kard.test.app.ui.adapter.TransactionsAdapter
import com.kard.test.app.ui.listener.TransactionClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransactionsFragment: Fragment(), TransactionClickListener {

    companion object {
        private val TAG = TransactionsFragment::class.java.simpleName
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
    private lateinit var type: TransactionOwnerType
    // Dependencies injection (Usually injected with dagger2)
    private lateinit var adapter: TransactionsAdapter

    // Views
    private lateinit var toolbar: Toolbar
    private lateinit var list: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyListView: TextView
    private lateinit var errorView: TextView

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
        emptyListView = view.findViewById(R.id.empty)
        errorView = view.findViewById(R.id.error)

        setupToolbar()
        setupList()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    // Private helper

    private fun loadData() {
        if (adapter.itemCount == 0 && !swipeToRefresh.isRefreshing) {
            progressBar.visibility = View.VISIBLE
        }

        KardNetworkClient().getTransactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Log.d(TAG, "On success, my email is: ${response.data()?.me()?.email()}")
                val transactions = response.data()?.me()?.transactions()?.edges()
                if (transactions != null && transactions.isNotEmpty()) {
                    displayList(transactions)
                }
                else {
                    displayEmptyScreen()
                }
                progressBar.visibility = View.GONE
                swipeToRefresh.isRefreshing = false
            }, { error ->
                displayError(error)
                progressBar.visibility = View.GONE
                swipeToRefresh.isRefreshing = false
            })
            .attachToPauseLifecycle(this)
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
        adapter = TransactionsAdapter(context, this)
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.adapter = adapter

        // Swipe to refresh
        swipeToRefresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = true
            loadData()
        }
    }

    // Displaying list methods

    private fun displayList(transactions: List<MeQuery.Edge>) {
        adapter.objects.clear()
        adapter.objects.addAll(transactions)
        adapter.notifyDataSetChanged()
        list.visibility = View.VISIBLE
        emptyListView.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    private fun displayEmptyScreen() {
        emptyListView.visibility = View.VISIBLE
        list.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    private fun displayError(error: Throwable) {
        errorView.visibility = View.VISIBLE
        emptyListView.visibility = View.GONE
        list.visibility = View.GONE
        Log.e(TAG, "On failure: ${error.localizedMessage}")
        error.printStackTrace()
    }

    // Adapter Listener

    override fun onTransactionClick(transaction: MeQuery.Edge) {
        Toast.makeText(context, transaction.node()?.id(), Toast.LENGTH_SHORT).show()
    }
}