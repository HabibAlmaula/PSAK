package com.fkammediacenter.psak.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fkammediacenter.psak.daoresponse.Alldonasi
import com.fkammediacenter.psak.utils.State
import com.fkammediacenter.psak.utils.api.BaseApiService
import com.fkammediacenter.psak.utils.api.UtilsApi
import io.reactivex.disposables.CompositeDisposable

class DonasiViewModel : ViewModel(){

    private val baseApiService : BaseApiService = UtilsApi.getAPIService()
    var donasiList : LiveData<PagedList<Alldonasi>>
    val compositeDisposable = CompositeDisposable()
    val pageSize = 5
    private val donasiDataSourceFactory : DonasiDataSourceFactory

    init {
        donasiDataSourceFactory =
            DonasiDataSourceFactory(
                compositeDisposable,
                baseApiService
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()

        donasiList = LivePagedListBuilder<Int, Alldonasi>(donasiDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<DonasiDataSource,
            State>(donasiDataSourceFactory.berandaDataSourceLiveData, DonasiDataSource::state)

    fun retry(){
        donasiDataSourceFactory.berandaDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return donasiList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }





}