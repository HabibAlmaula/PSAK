package com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fkammediacenter.psak.daoresponse.DonaturTetaplist
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import io.reactivex.disposables.CompositeDisposable


class DonaturTetapViewModel(donatur : String, idfr : String) : ViewModel(){

    private val baseApiService : BaseApiService = UtilsApi.getAPIService()
    var donaturList : LiveData<PagedList<DonaturTetaplist>>
    val compositeDisposable = CompositeDisposable()
    val pageSize = 5
    private val donaturTetapDataSourceFactory : DonaturTetapDataSourceFactory

    init {
        donaturTetapDataSourceFactory =
            DonaturTetapDataSourceFactory(
                compositeDisposable,
                baseApiService,
                donatur,
                idfr
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        donaturList = LivePagedListBuilder<Int, DonaturTetaplist>(donaturTetapDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<DonaturTetapDataSource,
            State>(donaturTetapDataSourceFactory.berandaDataSourceLiveData, DonaturTetapDataSource::state)

    fun retry(){
        donaturTetapDataSourceFactory.berandaDataSourceLiveData.value?.retry()
    }

    fun reload(){
        donaturTetapDataSourceFactory.berandaDataSourceLiveData.value?.invalidate()
    }

    fun listIsEmpty(): Boolean {
        return donaturList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}