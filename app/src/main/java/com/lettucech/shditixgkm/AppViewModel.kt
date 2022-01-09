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

private val TAG = AppViewModel::class.java.simpleName

class AppViewModel : ViewModel() {
    private val appDatabase: MutableLiveData<AppDatabase> by lazy {
        MutableLiveData()
    }

    fun getAppDatabase(): LiveData<AppDatabase> {
        return appDatabase
    }

    fun connectDatabase(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "start connecting database in thread ${Thread.currentThread()}")
            var database: AppDatabase? = null
            try {
                database =
                    Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
                Log.d(TAG, "database connected")
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
            appDatabase.postValue(database)
        }
    }

    override fun onCleared() {
        super.onCleared()
        appDatabase.value?.close()
    }
}