package com.fkammediacenter.psak.daoresponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DonaturKotakList(
    @SerializedName("alamat_outlet")
    val alamatOutlet: String,
    @SerializedName("alamat_pemilik")
    val alamatPemilik: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id_donatur")
    val idDonatur: String,
    @SerializedName("id_fr")
    val idFr: String,
    @SerializedName("jadwal_kunjungan")
    val jadwalKunjungan: String,
    @SerializedName("jenis_usaha")
    val jenisUsaha: String,
    @SerializedName("kode_akun")
    val kodeAkun: String,
    @SerializedName("kode_wilayah")
    val kodeWilayah: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("nama_outlet")
    val namaOutlet: String,
    @SerializedName("nama_pemilik")
    val namaPemilik: String,
    @SerializedName("no_hp")
    val noHp: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tgl_pasang_kotak")
    val tglPasangKotak: String,
    @SerializedName("type_kotak")
    val typeKotak: String
):Parcelable