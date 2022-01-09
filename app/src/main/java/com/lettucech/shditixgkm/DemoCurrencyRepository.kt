package com.lettucech.shditixgkm

import com.lettucech.currencylist.CurrencyInfo
import com.lettucech.currencylist.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DemoCurrencyRepository(private val currencyInfoDao: CurrencyInfoDao) : CurrencyRepository() {
    override suspend fun addCurrencyInfo(vararg currencyInfo: CurrencyInfo) {
        return withContext(Dispatchers.IO) {
            currencyInfoDao.insertAll(*currencyInfo)
        }
    }

    override suspend fun getAllCurrencyInfo(): List<CurrencyInfo> {
        return withContext(Dispatchers.IO) {
            currencyInfoDao.getAll()
        }
    }
}