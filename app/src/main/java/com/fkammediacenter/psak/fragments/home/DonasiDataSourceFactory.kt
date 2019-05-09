package com.fkammediacenter.psak.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fkammediacenter.psak.daoresponse.Alldonasi
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.disposables.CompositeDisposable

class DonasiDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val baseApiService: BaseApiService
) : DataSource.Factory<Int, Alldonasi>() {


    val berandaDataSourceLiveData = MutableLiveData<DonasiDataSource>()

    override fun create(): DataSource<Int, Alldonasi> {
        val berandaDataSource = DonasiDataSource(
            baseApiService,
            compositeDisposable
        )
        berandaDataSourceLiveData.postValue(berandaDataSource)
        return berandaDataSource
    }
}

