package com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fkammediacenter.psak.daoresponse.DonaturTetaplist
import com.fkammediacenter.psak.utils.api.BaseApiService
import io.reactivex.disposables.CompositeDisposable

class DonaturTetapDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val baseApiService: BaseApiService,
    private val donatur : String,
    private  val idfr : String
) : DataSource.Factory<Int, DonaturTetaplist>() {


    val berandaDataSourceLiveData = MutableLiveData<DonaturTetapDataSource>()

    override fun create(): DataSource<Int, DonaturTetaplist>? {
        val donaturTetapDataSource = DonaturTetapDataSource(
            baseApiService,
            compositeDisposable,
            donatur,
            idfr
        )
        berandaDataSourceLiveData.postValue(donaturTetapDataSource)
        return donaturTetapDataSource
    }
}

