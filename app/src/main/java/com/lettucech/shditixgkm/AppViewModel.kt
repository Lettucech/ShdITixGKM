package com.lettucech.shditixgkm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val appDatabase: MutableLiveData<AppDatabase> by lazy {
        MutableLiveData()
    }

    fun getAppDatabase(): LiveData<AppDatabase> {
        return appDatabase
    }

    fun connectDatabase(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            var database: AppDatabase? = null
            try {
                database =
                    Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
            } catch (e: Exception) {
                Log.d(this@AppViewModel.TAG, e.toString())
            }
            appDatabase.postValue(database)
        }
    }

    override fun onCleared() {
        super.onCleared()
        appDatabase.value?.close()
    }
}