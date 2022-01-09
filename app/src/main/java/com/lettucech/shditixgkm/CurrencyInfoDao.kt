package com.lettucech.shditixgkm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lettucech.currencylist.CurrencyInfo

@Dao
interface CurrencyInfoDao {
    @Query("SELECT * FROM currencyinfo")
    suspend fun getAll() : List<CurrencyInfo>

    @Insert
    suspend fun insertAll(vararg currencyInfo: CurrencyInfo)
}