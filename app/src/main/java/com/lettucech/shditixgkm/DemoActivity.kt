package com.lettucech.shditixgkm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding4.view.clicks
import com.lettucech.currencylist.CurrencyInfo
import com.lettucech.currencylist.CurrencyListFragment
import com.lettucech.currencylist.CurrencyRepository
import com.lettucech.currencylist.OnCurrencyInfoItemClickListener
import com.lettucech.shditixgkm.databinding.ActivityDemoBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private val TAG = DemoActivity::class.java.simpleName

class DemoActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDemoBinding
    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        viewBinding.btnSortCurrencyList.isEnabled = false

        viewBinding.btnLoadCurrencyList.clicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .subscribe {
                viewBinding.btnLoadCurrencyList.isEnabled = false
                viewBinding.pbLoading.visibility = View.VISIBLE
                connectDatabase()
            }

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.getAppDatabase().observe(this, { db ->
            viewBinding.pbLoading.visibility = View.GONE
            if (db == null) {
                onAppError()
            } else {
                val repository = DemoCurrencyRepository(db.currencyInfoDao())
                loadSampleCurrencyList(repository) {
                    showCurrencyList(repository)
                }
            }
        })
    }

    private fun connectDatabase() {
        appViewModel.connectDatabase(applicationContext)
    }

    private fun onAppError() {
        viewBinding.btnLoadCurrencyList.isEnabled = false
        viewBinding.btnSortCurrencyList.isEnabled = false
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flFragmentContainer,
                ErrorPageFragment()
            ).commit()
    }

    private fun loadSampleCurrencyList(repository: CurrencyRepository, onReadyToShow: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (repository.getAllCurrencyInfo().isEmpty()) {
                Log.d(
                    TAG,
                    "loading sample currency list into database in thread ${Thread.currentThread()}"
                )
                val currencyInfoList = Gson().fromJson<List<CurrencyInfo>>(
                    resources.openRawResource(R.raw.sample_data_currency_list).bufferedReader(),
                    object : TypeToken<List<CurrencyInfo>>() {}.type
                )
                repository.addCurrencyInfo(*currencyInfoList.toTypedArray())
            }
            launch(Dispatchers.Main) {
                onReadyToShow()
            }
        }
    }

    private fun showCurrencyList(repository: CurrencyRepository) {
        val currencyListFragment = CurrencyListFragment(repository)

        var clickEmitter: ObservableEmitter<CurrencyInfo>? = null
        Observable.create<CurrencyInfo> { emitter -> clickEmitter = emitter }
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .subscribe {
                showClickedCurrencyInfo(it)
            }
        currencyListFragment.onCurrencyInfoItemClickListener =
            object : OnCurrencyInfoItemClickListener {
                override fun onCurrencyInfoItemClick(currencyInfo: CurrencyInfo?) {
                    if (currencyInfo != null) {
                        clickEmitter?.onNext(currencyInfo)
                    }
                }
            }

        currencyListFragment.onListUpdatedListener =
            object : CurrencyListFragment.OnListUpdatedListener {
                override fun onListUpdated() {
                    viewBinding.btnSortCurrencyList.isEnabled = true
                }
            }

        viewBinding.btnSortCurrencyList.clicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .subscribe {
                viewBinding.btnSortCurrencyList.isEnabled = false
                currencyListFragment.sortCurrencyList()
            }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flFragmentContainer,
                currencyListFragment
            ).commit()
    }

    private fun showClickedCurrencyInfo(currencyInfo: CurrencyInfo?) {
        currencyInfo?.let {
            Snackbar.make(viewBinding.root, it.name + " clicked", Snackbar.LENGTH_SHORT).show()
        }
    }
}