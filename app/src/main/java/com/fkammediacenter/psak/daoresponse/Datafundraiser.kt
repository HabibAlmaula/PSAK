package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class Datafundraiser(
    @SerializedName("alamat")
    val alamat: String,
    @SerializedName("bbm")
    val bbm: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("foto")
    val foto: String,
    @SerializedName("id_fr")
    val idFr: String,
    @SerializedName("jk")
    val jk: String,
    @SerializedName("kode_wilayah")
    val kodeWilayah: String,
    @SerializedName("nama_lengkap_fr")
    val namaLengkapFr: String,
    @SerializedName("nama_panggilan")
    val namaPanggilan: String,
    @SerializedName("no_hp")
    val noHp: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("pendidikan")
    val pendidikan: String,
    @SerializedName("status_fr")
    val statusFr: String,
    @SerializedName("tempat_lahir")
    val tempatLahir: String,
    @SerializedName("tgl_lahir")
    val tglLahir: String,
    @SerializedName("tgl_masuk")
    val tglMasuk: String
)