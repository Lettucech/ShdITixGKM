package com.lettucech.currencylist

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CurrencyListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val currencyList: MutableList<CurrencyInfo> = mutableListOf()
    var onCurrencyInfoItemClickListener: OnCurrencyInfoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = CurrencyListItemView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        view.setOnClickListener {
            onCurrencyInfoItemClickListener?.onCurrencyInfoItemClick(view.currencyInfo)
        }
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as CurrencyListItemView).currencyInfo = currencyList[position]
    }

    override fun getItemCount(): Int = currencyList.size

    /**
     * invoking notifyDataSetChanged() because the whole list will be updated anyway
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setCurrencyInfo(vararg currencyInfo: CurrencyInfo) {
        currencyList.clear()
        currencyList.addAll(currencyInfo)
        notifyDataSetChanged()
    }
}