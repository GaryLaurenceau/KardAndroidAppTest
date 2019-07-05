package com.kard.test.app.ui.transactions

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kard.test.app.MeQuery
import com.kard.test.app.R
import com.kard.test.app.data.TransactionOwnerType
import com.kard.test.app.domain.KardNetworkClient
import com.kard.test.app.extensions.attachToPauseLifecycle
import com.kard.test.app.ui.transactions.adapter.TransactionsAdapter
import com.kard.test.app.ui.transactions.listener.TransactionClickListener
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
    private lateinit var transactionsViewModel: TransactionsViewModel

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

        // Retrieve fragment parameters
        type = TransactionOwnerType.fromBundle(arguments,
            EXTRA_TRANSACTION_OWNER_TYPE
        )

        // Get ViewModel for fragment
        transactionsViewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)
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

        // Setup UI components
        setupToolbar()
        setupList()

        // Update UI if needed
        if (transactionsViewModel.transactions.isNotEmpty()) {
            displayList(transactionsViewModel.transactions)
        }
    }

    override fun onResume() {
        super.onResume()
        // Every time fragment is displayed, fetch data
        loadData()
    }

    // Private helper

    private fun loadData() {
        // Update progress bar if needed
        if (adapter.itemCount == 0 && !swipeToRefresh.isRefreshing) {
            progressBar.visibility = View.VISIBLE
        }

        // Exec network call
        KardNetworkClient().getTransactions(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                Log.d(TAG, "On success, my email is: ${response.data()?.me()?.email()}")

                // Get transactions
                val transactions = response.data()?.me()?.transactions()?.edges()
                if (transactions != null && transactions.isNotEmpty()) {
                    // Save list to ViewModel
                    transactionsViewModel.transactions.clear()
                    transactionsViewModel.transactions.addAll(transactions)

                    // Update UI
                    displayList(transactions)
                }
                else {
                    // UI Empty Screen
                    transactionsViewModel.transactions.clear()
                    displayEmptyScreen()
                }
                swipeToRefresh.isRefreshing = false
            }, { error ->
                // Display and log error
                displayError(error)
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
        progressBar.visibility = View.GONE
    }

    private fun displayEmptyScreen() {
        emptyListView.visibility = View.VISIBLE
        list.visibility = View.GONE
        errorView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun displayError(error: Throwable) {
        Log.e(TAG, "On failure: ${error.localizedMessage}")
        errorView.text = error.localizedMessage
        errorView.visibility = View.VISIBLE
        emptyListView.visibility = View.GONE
        list.visibility = View.GONE
        progressBar.visibility = View.GONE
        error.printStackTrace()
    }

    // Adapter Listener

    override fun onTransactionClick(transaction: MeQuery.Edge) {
        Toast.makeText(context, transaction.node()?.description(), Toast.LENGTH_SHORT).show()
    }
}