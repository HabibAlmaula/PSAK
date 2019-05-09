package com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fkammediacenter.psak.daoresponse.DonaturKotakList
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.disposables.CompositeDisposable

class DonaturKotakDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val baseApiService: BaseApiService,
    private val donatur : String,
    private val idfr : String,
    private val cari: String
) : DataSource.Factory<Int, DonaturKotakList>() {


    val berandaDataSourceLiveData = MutableLiveData<DonaturKotakDataSource>()

    override fun create(): DataSource<Int, DonaturKotakList>? {
        val donaturKotakDataSource = DonaturKotakDataSource(
            baseApiService,
            compositeDisposable,
            donatur,
            idfr,
            cari
        )
        berandaDataSourceLiveData.postValue(donaturKotakDataSource)
        return donaturKotakDataSource


    }
}

