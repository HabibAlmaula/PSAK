package com.fkammediacenter.psak.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fkammediacenter.psak.daoresponse.Alldonasi
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class DonasiDataSource(
    private val baseApiService: BaseApiService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Alldonasi>(){

    var state : MutableLiveData<State> = MutableLiveData()
    private var retryCompletable : Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Alldonasi>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            baseApiService.getAllDonasi(1, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.alldonasi,
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Alldonasi>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            baseApiService.getAllDonasi(params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.alldonasi,
                            params.key + 1)
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Alldonasi>) {
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