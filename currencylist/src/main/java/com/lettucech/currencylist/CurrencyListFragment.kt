package com.lettucech.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lettucech.currencylist.databinding.FragmentCurrencyListBinding

class CurrencyListFragment : Fragment() {
    private lateinit var viewBinding: FragmentCurrencyListBinding
    private lateinit var currencyListAdapter: CurrencyListRecyclerViewAdapter
    private var currencyViewModel: CurrencyListViewModel? = null
    var currencyRepository: CurrencyRepository? = null
    var onCurrencyInfoItemClickListener: OnCurrencyInfoItemClickListener? = null
    var onListUpdatedListener: OnListUpdatedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCurrencyListBinding.inflate(inflater)

        currencyListAdapter = CurrencyListRecyclerViewAdapter().also {
            it.onCurrencyInfoItemClickListener = object : OnCurrencyInfoItemClickListener {
                override fun onCurrencyInfoItemClick(currencyInfo: CurrencyInfo?) {
                    onCurrencyInfoItemClickListener?.onCurrencyInfoItemClick(currencyInfo)
                }
            }
            viewBinding.rv.adapter = it
        }

        currencyListAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                onListUpdatedListener?.onListUpdated()
            }
        })

        currencyRepository?.let {
            currencyViewModel =
                ViewModelProvider(this, CurrencyListViewModelFactory(it)).get(
                    CurrencyListViewModel::class.java
                )
            currencyViewModel?.getCurrencyList()?.observe(
                viewLifecycleOwner,
                { currencyList ->
                    currencyListAdapter.setCurrencyInfo(*currencyList.toTypedArray())
                })
            currencyViewModel?.loadCurrencyList()
        }

        return viewBinding.root
    }

    fun sortCurrencyList() = currencyViewModel?.sortCurrencyList()

    interface OnListUpdatedListener {
        fun onListUpdated()
    }
}