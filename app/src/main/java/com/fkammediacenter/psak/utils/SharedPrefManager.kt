package com.fkammediacenter.psak.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPrefManager(context: Context) {

    internal var sp: SharedPreferences
    internal var spEditor: SharedPreferences.Editor

    fun getSPNama(): String? {
        return sp.getString(SP_NAMA, "")
    }

    fun getSPIdFr(): String? {
        return sp.getString(SP_IDFR, "")
    }
    fun getSPIdDonaturKotak():String?{
        return sp.getString(SP_IDDONATURKotak,"")
    }
    fun getSPDonatur(): String?{
        return sp.getString(SP_DONATUR, "")
    }

    fun getSPDonasi(): String?{
        return sp.getString(SP_DONASI, "")
    }

    fun getSPSudahLogin(): Boolean? {
        return sp.getBoolean(SP_SUDAH_LOGIN, false)
    }

    init {
        sp = context.getSharedPreferences(SP_PSAK_APP, Context.MODE_PRIVATE)
        spEditor = sp.edit()
    }


    fun saveSPString(keySP: String, value: String) {
        spEditor.putString(keySP, value)
        spEditor.commit()
    }
    fun deleteSpString(keySP: String) {
        spEditor.remove(keySP)
        spEditor.commit()
    }

    fun saveSPInt(keySP: String, value: Int) {
        spEditor.putInt(keySP, value)
        spEditor.commit()
    }

    fun saveSPBoolean(keySP: String, value: Boolean) {
        spEditor.putBoolean(keySP, value)
        spEditor.commit()
    }

    companion object {

        val SP_PSAK_APP = "spPsakApp"

        val SP_IDFR = "spIdFr"
        val SP_NAMA = "spNama"
        val SP_DONATUR = "spDonatur"
        val SP_IDDONATURKotak = "spIDDONATURKotak"
        val SP_DONASI = "spDonasi"
        val SP_SUDAH_LOGIN = "spSudahLogin"
    }
}
