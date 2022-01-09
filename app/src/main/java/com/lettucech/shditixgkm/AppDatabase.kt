package com.lettucech.shditixgkm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lettucech.currencylist.CurrencyInfo

@Database(entities = [CurrencyInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyInfoDao(): CurrencyInfoDao
}