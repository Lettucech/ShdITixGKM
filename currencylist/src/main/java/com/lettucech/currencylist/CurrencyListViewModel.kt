package com.lettucech.currencylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = CurrencyListViewModel::class.java.simpleName

class CurrencyListViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {
    private val currencyList: MutableLiveData<List<CurrencyInfo>> by lazy {
        MutableLiveData<List<CurrencyInfo>>()
    }
    private var sorting = false
    private var currentSortMode = SortMode.NONE

    fun getCurrencyList(): LiveData<List<CurrencyInfo>> {
        return currencyList
    }

    fun loadCurrencyList() {
        if (currencyList.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG, "loading currency list from repository in thread ${Thread.currentThread()}")
                currencyList.postValue(currencyRepository.getAllCurrencyInfo())
            }
        }
    }

    fun sortCurrencyList() {
        if (sorting) return
        sorting = true

        val targetSortMode = when (currentSortMode) {
            SortMode.NONE -> SortMode.ALPHABETIC_ASC
            SortMode.ALPHABETIC_ASC -> SortMode.ALPHABETIC_DESC
            SortMode.ALPHABETIC_DESC -> SortMode.ALPHABETIC_ASC
        }

        if (targetSortMode != SortMode.NONE) {
            val currentList = currencyList.value
            if (currentList != null && currentList.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d(TAG, "sorting currency list in thread ${Thread.currentThread()}")

                    val sortedList = when (targetSortMode) {
                        SortMode.ALPHABETIC_ASC -> currentList.sortedBy { it.name }
                        SortMode.ALPHABETIC_DESC -> currentList.sortedByDescending { it.name }
                        else -> return@launch
                    }
                    currentSortMode = targetSortMode

                    currencyList.postValue(sortedList)
                    sorting = false
                }
            }
        }
    }

    enum class SortMode {
        NONE, ALPHABETIC_ASC, ALPHABETIC_DESC
    }
}