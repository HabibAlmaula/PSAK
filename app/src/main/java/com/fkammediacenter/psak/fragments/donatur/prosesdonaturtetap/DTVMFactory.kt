package com.fkammediacenter.psak.fragments.donatur.prosesdonaturtetap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DTVMFactory( private val donatur: String, private val idfr: String ) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonaturTetapViewModel(donatur, idfr) as T
    }
}