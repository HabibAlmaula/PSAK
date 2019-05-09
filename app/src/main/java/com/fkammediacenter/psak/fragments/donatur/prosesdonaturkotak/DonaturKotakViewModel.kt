package com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable


class DonaturKotakViewModel(donatur : String, idfr : String, cari : String) : ViewModel(){

    private val baseApiService : BaseApiService = UtilsApi.getAPIService()

    var donaturList : LiveData<PagedList<DonaturKotakList>>
    val compositeDisposable = CompositeDisposable()
    val pageSize = 5
    private val donaturKotakDataSourceFactory : DonaturKotakDataSourceFactory

    init {
        donaturKotakDataSourceFactory = DonaturKotakDataSourceFactory(compositeDisposable,
            baseApiService,
            donatur,
            idfr,
            cari
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        donaturList = LivePagedListBuilder<Int, DonaturKotakList>(donaturKotakDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<DonaturKotakDataSource,
            State>(donaturKotakDataSourceFactory.berandaDataSourceLiveData, DonaturKotakDataSource::state)

    fun retry(){
        donaturKotakDataSourceFactory.berandaDataSourceLiveData.value?.retry()
    }

    fun reload(){
        donaturKotakDataSourceFactory.berandaDataSourceLiveData.value?.invalidate()

    }

    fun listIsEmpty(): Boolean {
        return donaturList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun clearDataSource() {
        donaturKotakDataSourceFactory.create()
        createPagedObservable()
    }

    fun createPagedObservable(){
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        donaturList = LivePagedListBuilder<Int, DonaturKotakList>(donaturKotakDataSourceFactory, config).build()
    }


}