package com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fkammediacenter.psak.daoresponse.DonaturTetaplist
import com.fkammediacenter.psak.utils.SharedPrefManager
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class DonaturTetapDataSource(
    private val baseApiService: BaseApiService,
    private val compositeDisposable: CompositeDisposable,
    private val donatur : String,
    private val idfr : String
) : PageKeyedDataSource<Int, DonaturTetaplist>(){
    var state : MutableLiveData<State> = MutableLiveData()
    private var retryCompletable : Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DonaturTetaplist>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            baseApiService.getAllDonaturTetap(donatur,idfr, 1, params.requestedLoadSize)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DonaturTetaplist>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            baseApiService.getAllDonaturTetap(donatur,idfr,params.key, params.requestedLoadSize)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DonaturTetaplist>) {
    }

    private fun updateState(State: State){
        this.state.postValue(State)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }


}