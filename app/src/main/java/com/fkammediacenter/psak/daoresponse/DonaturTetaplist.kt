package com.fkammediacenter.psak.daoresponse

import com.google.gson.annotations.SerializedName

data class DonaturTetaplist(
    @SerializedName("alamat_kantor")
    val alamatKantor: String,
    @SerializedName("alamat_rumah")
    val alamatRumah: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id_donatur")
    val idDonatur: String,
    @SerializedName("id_fr")
    val idFr: String,
    @SerializedName("jk")
    val jk: String,
    @SerializedName("kode_akun")
    val kodeAkun: String,
    @SerializedName("kode_wilayah")
    val kodeWilayah: String,
    @SerializedName("nama_donatur")
    val namaDonatur: String,
    @SerializedName("nama_panggilan")
    val namaPanggilan: String,
    @SerializedName("no_hp")
    val noHp: String,
    @SerializedName("pekerjaan")
    val pekerjaan: String,
    @SerializedName("pendidikan")
    val pendidikan: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tempat_lahir")
    val tempatLahir: String,
    @SerializedName("tgl_gabung")
    val tglGabung: String,
    @SerializedName("tgl_lahir")
    val tglLahir: String
)