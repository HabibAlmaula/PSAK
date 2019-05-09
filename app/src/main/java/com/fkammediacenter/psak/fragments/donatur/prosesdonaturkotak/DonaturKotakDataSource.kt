package com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class DonaturKotakDataSource(
    private val baseApiService: BaseApiService,
    private var compositeDisposable: CompositeDisposable? = null,
    private val donatur: String,
    private val idfr: String,
    private val cari: String
) : PageKeyedDataSource<Int, DonaturKotakList>(){
    var state : MutableLiveData<State> = MutableLiveData()
    private var retryCompletable : Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DonaturKotakList>) {
        Log.d("CARI", cari)
        updateState(State.LOADING)
        if (cari !=""){
            Log.d("MENCARIINITIAL","BERHASIL IMPLEMENTASI")
            compositeDisposable?.add(
                baseApiService.searchDonaturKotak(donatur,idfr,cari,1, params.requestedLoadSize)
                    .subscribe(
                        { response ->
                            updateState(State.DONE)
                            callback.onResult(response.donaturlist,
                                null,2
                            )
                        },
                        {
                            updateState(State.ERROR)
                            setRetry(Action { loadInitial(params, callback) })
                        }
                    )
            )
        }
        if (cari.isEmpty() || cari == ""){
            Log.d("TIDAKMENCARIINITIAL","GAGAL IMPLEMENTASI")
            compositeDisposable?.add(
                baseApiService.getAllDonaturKotak(donatur,idfr, 1, params.requestedLoadSize)
                    .subscribe(
                        { response ->
                            updateState(State.DONE)
                            callback.onResult(response.donaturlist,
                                null,2
                            )
                        },
                        {
                            updateState(State.ERROR)
                            setRetry(Action { loadInitial(params, callback) })
                        }
                    )
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DonaturKotakList>) {
        Log.d("CARI", cari)
        updateState(State.LOADING)

        if (cari.isEmpty() || cari == ""){
            Log.d("TIDAKMENCARIAFTER","GAGAL IMPLEMENTASI")

            compositeDisposable?.add(
                baseApiService.getAllDonaturKotak(donatur,idfr,params.key, params.requestedLoadSize)
                    .subscribe(
                        { response ->
                            updateState(State.DONE)
                            callback.onResult(response.donaturlist,
                                params.key + 1)
                        },
                        {
                            updateState(State.ERROR)
                            setRetry(Action { loadAfter(params, callback) })
                        }
                    )
            )
        }
        if (cari != ""){
            Log.d("MENCARIAFTER","BERHASIL IMPLEMENTASI")

            compositeDisposable?.add(
                baseApiService.searchDonaturKotak(donatur,idfr,cari,params.key, params.requestedLoadSize)
                    .subscribe(
                        { response ->
                            updateState(State.DONE)
                            callback.onResult(response.donaturlist,
                                params.key + 1)
                        },
                        {
                            updateState(State.ERROR)
                            setRetry(Action { loadAfter(params, callback) })
                        }

                    )
            )
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DonaturKotakList>) {
    }

    private fun updateState(State: State){
        this.state.postValue(State)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable?.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }


    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }


}