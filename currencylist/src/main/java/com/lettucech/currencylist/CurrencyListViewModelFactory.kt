package com.lettucech.currencylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyListViewModelFactory(private val currencyRepository: CurrencyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyListViewModel::class.java)) {
            return CurrencyListViewModel(currencyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}