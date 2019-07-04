package com.kard.test.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kard.test.app.R
import com.kard.test.app.data.TransactionOwnerType
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
    private lateinit var type: TransactionOwnerType
    private lateinit var adapter: TransactionsAdapter

    // Views
    private lateinit var toolbar: Toolbar
    private lateinit var list: RecyclerView

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

        setupList()
    }

    private fun setupList() {

    }

}