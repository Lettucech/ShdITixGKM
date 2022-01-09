package com.lettucech.currencylist

abstract class CurrencyRepository {
    abstract suspend fun addCurrencyInfo(vararg currencyInfo: CurrencyInfo)

    abstract suspend fun getAllCurrencyInfo(): List<CurrencyInfo>
}