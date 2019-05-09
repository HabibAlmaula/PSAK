package com.fkammediacenter.psak.fragments.donatur.prosesdonaturkotak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap.DonaturTetapViewModel

class DKVMFactory( private val donatur: String, private val idfr: String, private val cari : String) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonaturKotakViewModel(donatur, idfr, cari) as T
    }
}