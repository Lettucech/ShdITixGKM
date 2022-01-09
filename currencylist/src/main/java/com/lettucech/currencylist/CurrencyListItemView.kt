package com.lettucech.currencylist

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lettucech.currencylist.databinding.ViewCurrencyListItemBinding


class CurrencyListItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: ViewCurrencyListItemBinding =
        ViewCurrencyListItemBinding.inflate(LayoutInflater.from(context), this)

    var currencyInfo: CurrencyInfo? = null
        set(value) {
            field = value
            binding.tvInitial.text = field?.symbol?.substring(0, 1)
            binding.tvName.text = field?.name
            binding.tvSymbol.text = field?.symbol
        }

    init {
        val padding = resources.getDimensionPixelSize(R.dimen.currency_list_item_view_padding)
        binding.root.setPadding(padding, padding, padding, padding)

        val typedValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            typedValue,
            true
        )
        binding.root.setBackgroundResource(typedValue.resourceId)
    }

}