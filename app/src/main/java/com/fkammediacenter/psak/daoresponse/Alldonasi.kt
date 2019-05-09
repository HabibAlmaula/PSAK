package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class Alldonasi(
    @SerializedName("id_donasi")
    val idDonasi: String,
    @SerializedName("id_donatur")
    val idDonatur: String,
    @SerializedName("id_fr")
    val idFr: String,
    @SerializedName("keterangan")
    val keterangan: String,
    @SerializedName("kode_akun")
    val kodeAkun: String,
    @SerializedName("kode_wilayah")
    val kodeWilayah: String,
    @SerializedName("nama_donatur")
    val namaDonatur: String,
    @SerializedName("nominal_donasi")
    val nominalDonasi: String,
    @SerializedName("status_donasi")
    val statusDonasi: String,
    @SerializedName("status_donatur")
    val statusDonatur: String,
    @SerializedName("tag_line")
    val tagLine: String,
    @SerializedName("tgl_kunjungan")
    val tglKunjungan: String,
    @SerializedName("tujuan_donasi")
    val tujuanDonasi: String,
    @SerializedName("waktu_input")
    val waktuInput: String
)